package se.company.platform.deployment.domain.port.out;

import se.company.platform.deployment.domain.Environment;
import se.company.platform.deployment.domain.DeploymentMergeRequest;
import se.company.platform.deployment.domain.DeploymentMergeRequest.DeploymentMergeRequestId;
import se.company.platform.deployment.domain.ServiceIdentifier;
import se.company.platform.deployment.domain.ProjectLocator;
import se.company.platform.deployment.domain.Version;

public interface GitOpsConfigRepositoryPort {

        Version getDeployedVersion(ProjectLocator serviceLocator, ServiceIdentifier service, Environment environment);

        /**
         * Update ArgoCD manifest with new version on a new branch with an associated
         * merge request.
         */
        DeploymentMergeRequest openDeploymentMergeRequest(
                        ProjectLocator projectLocator,
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
        void updateDeploymentMergeRequestDescription(ProjectLocator projectLocator, DeploymentMergeRequestId id,
                        String markdownBody);

        /**
         * Add an informational note/comment to the MR.
         */
        void addDeploymentMergeRequestNote(ProjectLocator projectLocator, DeploymentMergeRequestId id, String note);

        /**
         * Auto-approve the deployment MR (using the bot user) if policy allows it.
         */
        void approveDeploymentMergeRequest(ProjectLocator projectLocator, DeploymentMergeRequestId id);

        /**
         * Merge the deployment MR (for GitOps) when policy decides.
         */
        void mergeDeploymentMergeRequest(ProjectLocator projectLocator, DeploymentMergeRequestId id);
}