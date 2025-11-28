package se.company.platform.deployment.domain;

public record DeploymentMergeRequest(
        DeploymentMergeRequestId id,
        String url,
        String branchName) {
    public record DeploymentMergeRequestId(Long id) {

    }
}