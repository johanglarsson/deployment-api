package se.company.platform.deployment.integrations.gitops;

import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.MergeRequestApi;
import org.gitlab4j.api.NotesApi;
import org.gitlab4j.api.RepositoryApi;
import org.gitlab4j.api.RepositoryFileApi;
import org.gitlab4j.api.models.MergeRequest;
import org.gitlab4j.api.models.MergeRequestFilter;
import org.gitlab4j.api.models.MergeRequestParams;
import org.gitlab4j.api.models.RepositoryFile;
import org.gitlab4j.models.Constants.MergeRequestState;

import static java.util.Objects.requireNonNull;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import se.company.platform.deployment.domain.Environment;
import se.company.platform.deployment.domain.DeploymentMergeRequest;
import se.company.platform.deployment.domain.DeploymentMergeRequest.DeploymentMergeRequestId;
import se.company.platform.deployment.domain.ServiceIdentifier;
import se.company.platform.deployment.domain.ProjectLocator;
import se.company.platform.deployment.domain.Version;
import se.company.platform.deployment.domain.port.out.GitOpsConfigRepositoryPort;

public class GitLabGitOpsConfigRepositoryAdapter implements GitOpsConfigRepositoryPort {

    private final RepositoryFileApi repositoryFileApi;
    private final RepositoryApi repositoryApi;
    private final MergeRequestApi mergeRequestApi;
    private final NotesApi notesApi;
    private final ArgoCdManifestParser argoCdManifestParser;

    public GitLabGitOpsConfigRepositoryAdapter(
            final RepositoryFileApi repositoryFileApi,
            final RepositoryApi repositoryApi,
            final MergeRequestApi mergeRequestApi,
            final NotesApi notesApi,
            final ArgoCdManifestParser argoCdManifestParser) {
        this.repositoryFileApi = requireNonNull(repositoryFileApi);
        this.repositoryApi = requireNonNull(repositoryApi);
        this.mergeRequestApi = requireNonNull(mergeRequestApi);
        this.notesApi = requireNonNull(notesApi);
        this.argoCdManifestParser = requireNonNull(argoCdManifestParser);
    }

    @Override
    public Version getDeployedVersion(ProjectLocator projectLocator, ServiceIdentifier service,
            Environment environment) {
        return repositoryFileApi.getOptionalFile(projectLocator.path(), service.id(), "main")
                .map(file -> argoCdManifestParser.readCurrentVersion(file.getDecodedContentAsString()))
                .orElseThrow(() -> new IllegalStateException("Unable to find file"));
    }

    @Override
    public DeploymentMergeRequest openDeploymentMergeRequest(
            ProjectLocator projectLocator,
            ServiceIdentifier service,
            Environment environment,
            Version fromVersion,
            Version toVersion,
            String branchName,
            String mergeRequestTitle,
            String initialDescription) {

        repositoryFileApi.getOptionalFile(projectLocator.path(), service.id(), "main")
                .ifPresentOrElse(file -> updateManifestAndCreateBranch(file, projectLocator, branchName, toVersion),
                        () -> new IllegalStateException());

        try {
            MergeRequestFilter filter = new MergeRequestFilter();
            filter.setSourceBranch(branchName);
            filter.setState(MergeRequestState.OPENED);
            Optional<MergeRequest> possibleMergeRequest = mergeRequestApi.getMergeRequests(projectLocator, filter)
                    .stream()
                    .findFirst();

            if (!possibleMergeRequest.isEmpty()) {
                return new DeploymentMergeRequest(new DeploymentMergeRequestId(possibleMergeRequest.get().getIid()),
                        possibleMergeRequest.get().getWebUrl(),
                        possibleMergeRequest.get().getSourceBranch());

            }
            MergeRequestParams params = new MergeRequestParams();
            params.withDescription(initialDescription)
                    .withTitle(mergeRequestTitle)
                    .withRemoveSourceBranch(true)
                    .withSourceBranch(branchName)
                    .withSquash(true)
                    .withTargetBranch("main");
            MergeRequest mergeRequest = mergeRequestApi.createMergeRequest(projectLocator, params);
            return toDomain(mergeRequest);
        } catch (GitLabApiException e) {
            throw new IllegalStateException("Unable to create a merge request", e);
        }
    }

    private void updateManifestAndCreateBranch(
            RepositoryFile repositoryFile,
            ProjectLocator projectLocator,
            String branchName, Version version) {

        String manifest = argoCdManifestParser.updateVersion(repositoryFile.getDecodedContentAsString(), version);
        repositoryFile.setContent(manifest);
        try {
            repositoryApi.getOptionalBranch(projectLocator, branchName)
                    .orElse(repositoryApi.createBranch(projectLocator.path(), branchName, "main"));
            repositoryFileApi.updateFile(projectLocator.path(), repositoryFile, branchName,
                    "Updated version " + version.value());
        } catch (GitLabApiException e) {
            throw new IllegalStateException("Unable to update manifest", e);
        }
    }

    @Override
    public void updateDeploymentMergeRequestDescription(
            ProjectLocator projectLocator,
            DeploymentMergeRequestId id,
            String markdownBody) {
        MergeRequestParams params = new MergeRequestParams();
        params.withDescription(markdownBody);
        try {
            mergeRequestApi.updateMergeRequest(projectLocator.path(), id.id(), params);
        } catch (GitLabApiException e) {
            throw new IllegalStateException("Unable to update merge request", e);
        }
    }

    @Override
    public void addDeploymentMergeRequestNote(
            ProjectLocator projectLocator,
            DeploymentMergeRequestId id,
            String note) {
        try {
            notesApi.createMergeRequestNote(projectLocator.path(), id.id(), note, new Date(), false);
        } catch (GitLabApiException e) {
            throw new IllegalStateException("Unable to create a new note", e);
        }
    }

    @Override
    public void approveDeploymentMergeRequest(
            ProjectLocator projectLocator,
            DeploymentMergeRequestId id) {
        try {
            mergeRequestApi.approveMergeRequest(projectLocator.path(), id.id(), null);
        } catch (GitLabApiException e) {
            throw new IllegalStateException("Unable to approve merge request");
        }
    }

    @Override
    public void mergeDeploymentMergeRequest(ProjectLocator projectLocator, DeploymentMergeRequestId id) {
        try {
            mergeRequestApi.acceptMergeRequest(projectLocator, id.id());
        } catch (GitLabApiException e) {
            throw new IllegalStateException("Unable to merge a merge request", e);
        }
    }

    public DeploymentMergeRequest toDomain(MergeRequest mergeRequest) {
        return new DeploymentMergeRequest(new DeploymentMergeRequestId(mergeRequest.getIid()), mergeRequest.getWebUrl(),
                mergeRequest.getSourceBranch());
    }

}
