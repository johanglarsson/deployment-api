package se.company.platform.deployment.integrations.gitops;

import org.gitlab4j.api.GitLabApi;
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

import se.company.platform.deployment.domain.Environment;
import se.company.platform.deployment.domain.DeploymentMergeRequest;
import se.company.platform.deployment.domain.DeploymentMergeRequestId;
import se.company.platform.deployment.domain.ServiceIdentifier;
import se.company.platform.deployment.domain.ServiceLocator;
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
    public Version getDeployedVersion(ServiceLocator serviceLocator, ServiceIdentifier service,
            Environment environment) {
        return repositoryFileApi.getOptionalFile(serviceLocator.path(), service.id(), "main")
                .map(file -> argoCdManifestParser.readCurrentVersion(file.getDecodedContentAsString()))
                .orElseThrow(() -> new IllegalStateException("Unable to find file"));
    }

    @Override
    public DeploymentMergeRequest openDeploymentMergeRequest(
            ServiceLocator serviceLocator,
            ServiceIdentifier service,
            Environment environment,
            Version fromVersion,
            Version toVersion,
            String branchName,
            String mergeRequestTitle,
            String initialDescription) {

        repositoryFileApi.getOptionalFile(serviceLocator.path(), service.id(), "main")
                .ifPresentOrElse(file -> updateManifestAndCreateBranch(file, serviceLocator, branchName, toVersion),
                        () -> new IllegalStateException());

        try {
            MergeRequestFilter filter = new MergeRequestFilter();
            filter.setSourceBranch(branchName);
            filter.setState(MergeRequestState.OPENED);
            List<MergeRequest> mergeRequests = mergeRequestApi.getMergeRequests(serviceLocator, filter);
            if (!mergeRequests.isEmpty()) {
                return new DeploymentMergeRequest(mergeRequests.get(0).getIid(), mergeRequests.get(0).getWebUrl(),
                        mergeRequests.get(0).getSourceBranch());
            }
            MergeRequestParams params = new MergeRequestParams();
            params.withDescription(initialDescription)
                    .withTitle(mergeRequestTitle)
                    .withRemoveSourceBranch(true)
                    .withSourceBranch(branchName)
                    .withSquash(true)
                    .withTargetBranch("main");
            MergeRequest mergeRequest = mergeRequestApi.createMergeRequest(serviceLocator, params);
            return new DeploymentMergeRequest(mergeRequest.getIid(), mergeRequest.getWebUrl(), branchName);
        } catch (GitLabApiException e) {
            throw new IllegalStateException("Unable to create a merge request", e);
        }
    }

    private void updateManifestAndCreateBranch(
            RepositoryFile repositoryFile,
            ServiceLocator serviceLocator,
            String branchName, Version version) {

        String manifest = argoCdManifestParser.updateVersion(repositoryFile.getDecodedContentAsString(), version);
        repositoryFile.setContent(manifest);
        try {
            repositoryApi.getOptionalBranch(serviceLocator, branchName)
                    .orElse(repositoryApi.createBranch(serviceLocator.path(), branchName, "main"));
            repositoryFileApi.updateFile(serviceLocator.path(), repositoryFile, branchName,
                    "Updated version " + version.value());
        } catch (GitLabApiException e) {
            throw new IllegalStateException("Unable to update manifest", e);
        }
    }

    @Override
    public void updateDeploymentMergeRequestDescription(
            ServiceLocator serviceLocator,
            DeploymentMergeRequestId id,
            String markdownBody) {
        MergeRequestParams params = new MergeRequestParams();
        params.withDescription(markdownBody);
        try {
            mergeRequestApi.updateMergeRequest(serviceLocator.path(), id.id(), params);
        } catch (GitLabApiException e) {
            throw new IllegalStateException("Unable to update merge request", e);
        }
    }

    @Override
    public void addDeploymentMergeRequestNote(
            ServiceLocator serviceLocator,
            DeploymentMergeRequestId id,
            String note) {
        try {
            notesApi.createMergeRequestNote(serviceLocator.path(), id.id(), note, new Date(), false);
        } catch (GitLabApiException e) {
            throw new IllegalStateException("Unable to create a new note", e);
        }
    }

    @Override
    public void approveDeploymentMergeRequest(
            ServiceLocator serviceLocator,
            DeploymentMergeRequestId id) {
        try {
            mergeRequestApi.approveMergeRequest(serviceLocator.path(), id.id(), null);
        } catch (GitLabApiException e) {
            throw new IllegalStateException("Unable to approve merge request");
        }
    }

    @Override
    public void mergeDeploymentMergeRequest(ServiceLocator serviceLocator, DeploymentMergeRequestId id) {
        try {
            mergeRequestApi.acceptMergeRequest(serviceLocator, id.id());
        } catch (GitLabApiException e) {
            throw new IllegalStateException("Unable to merge a merge request", e);
        }
    }

}
