package se.company.platform.deployment.application.api.dtos;

import java.util.List;

public record PromoteRequestDto(
        String version,
        String sourceProject,
        String sourceRef,
        String configProject,
        List<Environment> environments) {
}
