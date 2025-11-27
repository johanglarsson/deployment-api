package se.company.platform.deployment.domain.port.out;

import se.company.platform.deployment.domain.Environment;
import se.company.platform.deployment.domain.DeploymentMergeRequest;
import se.company.platform.deployment.domain.DeploymentMergeRequestId;
import se.company.platform.deployment.domain.ServiceIdentifier;
import se.company.platform.deployment.domain.ProjectLocator;
import se.company.platform.deployment.domain.Version;

public interface GitOpsConfigRepositoryPort {

    Version getDeployedVersion(ProjectLocator serviceLocator, ServiceIdentifier service, Environment environment);

    /**
     * Update ArgoCD manifest with new version on a new branch.
     */
    DeploymentMergeRequest openDeploymentMergeRequest(
            ProjectLocator serviceLocator,
            ServiceIdentifier service,
            Environment environment,
            Version fromVersion,
            Version toVersion,
            String branchName,
            String mergeRequestTitle,
            String initialDescription);

    /**
     * Update MR description with evidence summary / classification.
     */
    void updateDeploymentMergeRequestDescription(ProjectLocator locator, DeploymentMergeRequestId id,
            String markdownBody);

    /**
     * Add an informational note/comment to the MR.
     */
    void addDeploymentMergeRequestNote(ProjectLocator locator, DeploymentMergeRequestId id, String note);

    /**
     * Auto-approve the deployment MR (using the bot user) if policy allows it.
     */
    void approveDeploymentMergeRequest(ProjectLocator locator, DeploymentMergeRequestId id);

    /**
     * Merge the deployment MR (for GitOps) when policy decides.
     */
    void mergeDeploymentMergeRequest(ProjectLocator locator, DeploymentMergeRequestId id);
}