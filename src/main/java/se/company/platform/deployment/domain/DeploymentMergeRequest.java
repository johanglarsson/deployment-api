package se.company.platform.deployment.domain;

public record DeploymentMergeRequest(
                Long id,
                String url,
                String branchName) {
}