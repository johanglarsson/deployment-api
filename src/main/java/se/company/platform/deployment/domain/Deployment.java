package se.company.platform.deployment.domain;

import java.util.UUID;

public class Deployment {
    private final DeploymentId id;
    private final ProjectLocator locator;
    private final ServiceIdentifier service;
    private final Environment environment;
    private final Version previousVersion;
    private final Version targetVersion;
    private final ChangeSummary changeSummary;
    private final ApprovalDecision approvalDecision;
    private final DeploymentMergeRequest mergeRequestInfo;

    public Deployment(DeploymentId id, ServiceIdentifier service, ProjectLocator locator,
            Environment environment, Version previousVersion,
            Version targetVersion, ChangeSummary changeSummary, ApprovalDecision approvalDecision,
            DeploymentMergeRequest mergeRequestInfo) {
        this.id = id;
        this.service = service;
        this.locator = locator;
        this.environment = environment;
        this.previousVersion = previousVersion;
        this.targetVersion = targetVersion;
        this.changeSummary = changeSummary;
        this.approvalDecision = approvalDecision;
        this.mergeRequestInfo = mergeRequestInfo;
    }

}