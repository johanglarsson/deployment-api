package se.company.platform.deployment.domain.port.out;

import se.company.platform.deployment.domain.Environment;
import se.company.platform.deployment.domain.MergeRequestInfo;
import se.company.platform.deployment.domain.ServiceIdentifier;
import se.company.platform.deployment.domain.Version;

public interface ConfigRepositoryPort {

    Version getCurrentVersion(ServiceIdentifier service, Environment environment);

    /**
     * Update ArgoCD manifest with new version on a new branch.
     */
    MergeRequestInfo updateVersionAndOpenMergeRequest(
            ServiceIdentifier service,
            Environment environment,
            Version fromVersion,
            Version toVersion,
            String branchName,
            String mergeRequestTitle,
            String initialDescription);
}