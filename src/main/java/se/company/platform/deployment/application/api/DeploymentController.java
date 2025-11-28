package se.company.platform.deployment.application.api;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import se.company.platform.deployment.application.api.dtos.PromoteRequestDto;
import se.company.platform.deployment.application.api.dtos.PromoteResponseDto;
import se.company.platform.deployment.domain.port.in.DeploymentUseCase;
import se.company.platform.deployment.domain.port.in.DeploymentUseCase.DeployCommand;

@RestController
class DeploymentController {
    private final Logger logger = LoggerFactory.getLogger(DeploymentController.class);

    private final DeploymentUseCase deploymentUseCase;

    public DeploymentController(DeploymentUseCase deploymentUseCase) {
        this.deploymentUseCase = Objects.requireNonNull(deploymentUseCase);
    }

    @PostMapping
    PromoteResponseDto promote(final PromoteRequestDto request) {

        logger.info("Promoted to enviornments Y,X,Z");
        DeployCommand command = new DeployCommand();
        deploymentUseCase.deploy(null);
        return null;
    }
}
