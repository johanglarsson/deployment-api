package se.company.platform.deployment.domain.port.in;

import se.company.platform.deployment.domain.Deployment;
import se.company.platform.deployment.domain.Environment;
import se.company.platform.deployment.domain.ServiceIdentifier;
import se.company.platform.deployment.domain.ProjectLocator;
import se.company.platform.deployment.domain.Version;

public interface DeploymentUseCase {
    Deployment deploy(DeployCommand command);

    public record DeployCommand(
            ProjectLocator appSourceLocator,
            ProjectLocator configLocator,
            ServiceIdentifier service,
            Environment environment,
            Version targetVersion,
            MergeWhenAllChecksHasPassed mergeWhenAllChecksHasPassed) {
    }

    public record MergeWhenAllChecksHasPassed(Boolean value) {

    }

    public record ServiceIdentifier(String id) {

    }

    public record ProjectLocator(String path) {

    }

}
