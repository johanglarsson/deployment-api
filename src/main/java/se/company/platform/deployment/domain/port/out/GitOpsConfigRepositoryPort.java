package se.company.platform.deployment.domain.port.out;

import se.company.platform.deployment.domain.Environment;
import se.company.platform.deployment.domain.DeploymentMergeRequest;
import se.company.platform.deployment.domain.DeploymentMergeRequestId;
import se.company.platform.deployment.domain.ServiceIdentifier;
import se.company.platform.deployment.domain.ServiceLocator;
import se.company.platform.deployment.domain.Version;

public interface GitOpsConfigRepositoryPort {

    Version getDeployedVersion(ServiceLocator serviceLocator, ServiceIdentifier service, Environment environment);

    /**
     * Update ArgoCD manifest with new version on a new branch.
     */
    DeploymentMergeRequest openDeploymentMergeRequest(
            ServiceLocator serviceLocator,
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
    void updateDeploymentMergeRequestDescription(ServiceLocator locator, DeploymentMergeRequestId id,
            String markdownBody);

    /**
     * Add an informational note/comment to the MR.
     */
    void addDeploymentMergeRequestNote(ServiceLocator locator, DeploymentMergeRequestId id, String note);

    /**
     * Auto-approve the deployment MR (using the bot user) if policy allows it.
     */
    void approveDeploymentMergeRequest(ServiceLocator locator, DeploymentMergeRequestId id);

    /**
     * Merge the deployment MR (for GitOps) when policy decides.
     */
    void mergeDeploymentMergeRequest(ServiceLocator locator, DeploymentMergeRequestId id);
}