package se.company.platform.deployment.application.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import se.company.platform.deployment.application.api.dtos.PromoteRequestDto;
import se.company.platform.deployment.application.api.dtos.PromoteResponseDto;

@RestController
class DeploymentController {
    private final Logger logger = LoggerFactory.getLogger(DeploymentController.class);

    @PostMapping
    PromoteResponseDto promote(final PromoteRequestDto request) {

        logger.info("Promoted to enviornments Y,X,Z");
        return null;
    }
}
