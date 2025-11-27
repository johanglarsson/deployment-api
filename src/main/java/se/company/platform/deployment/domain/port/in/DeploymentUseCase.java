package se.company.platform.deployment.domain.port.in;

import se.company.platform.deployment.domain.Deployment;
import se.company.platform.deployment.domain.Environment;
import se.company.platform.deployment.domain.ServiceIdentifier;
import se.company.platform.deployment.domain.ServiceLocator;
import se.company.platform.deployment.domain.Version;

public interface DeploymentUseCase {
    Deployment deploy(DeployCommand command);

    record DeployCommand(
            ServiceLocator locator,
            ServiceIdentifier service,
            Environment environment,
            Version targetVersion) {
    }
}
