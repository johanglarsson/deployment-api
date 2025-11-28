package se.company.platform.deployment.domain.service;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import se.company.platform.deployment.domain.ApprovalDecision;
import se.company.platform.deployment.domain.ChangeSummary;
import se.company.platform.deployment.domain.CommitRange;
import se.company.platform.deployment.domain.Deployment;
import se.company.platform.deployment.domain.Deployment.DeploymentId;
import se.company.platform.deployment.domain.DeploymentMergeRequest;
import se.company.platform.deployment.domain.Version;
import se.company.platform.deployment.domain.port.in.DeploymentUseCase;
import se.company.platform.deployment.domain.port.out.GitOpsConfigRepositoryPort;
import se.company.platform.deployment.domain.port.out.CiEvidencePort;
import se.company.platform.deployment.domain.port.out.AppSourceRepositoryPort;

public final class DeploymentService implements DeploymentUseCase {

        private final GitOpsConfigRepositoryPort gitOpsConfigRepositoryPort;
        private final ChangeSummaryService changeSummaryService;
        private final ChangeSummaryMarkdownRenderer changeSummaryMarkdownRenderer;

        public DeploymentService(
                        GitOpsConfigRepositoryPort gitOpsConfigRepositoryPort,
                        CiEvidencePort ciEvidencePort,
                        AppSourceRepositoryPort appSourceRepositoryPort,
                        ChangeSummaryService changeSummaryService,
                        ChangeSummaryMarkdownRenderer changeSummaryMarkdownRenderer) {
                this.gitOpsConfigRepositoryPort = requireNonNull(gitOpsConfigRepositoryPort);
                this.changeSummaryService = requireNonNull(changeSummaryService);
                this.changeSummaryMarkdownRenderer = requireNonNull(changeSummaryMarkdownRenderer);
        }

        @Override
        public Deployment deploy(DeployCommand command) {
                Version currentVersion = gitOpsConfigRepositoryPort.getDeployedVersion(command.configLocator(),
                                command.service(),
                                command.environment());

                CommitRange range = new CommitRange(currentVersion, command.targetVersion());
                ChangeSummary changeSummary = changeSummaryService.collect(range, command.appSourceLocator());
                String markdown = changeSummaryMarkdownRenderer.render(changeSummary);
                DeploymentMergeRequest deploymentMergeRequest = gitOpsConfigRepositoryPort.openDeploymentMergeRequest(
                                command.configLocator(),
                                command.service(),
                                command.environment(),
                                currentVersion,
                                command.targetVersion(),
                                "Branchname",
                                "Deploy " + command.service() + " " + command.targetVersion(),
                                markdown);
                if (command.environment().isNonProd()) {
                        gitOpsConfigRepositoryPort.approveDeploymentMergeRequest(command.configLocator(),
                                        deploymentMergeRequest.id());
                        if (command.mergeWhenAllChecksHasPassed().value()) {
                                gitOpsConfigRepositoryPort.mergeDeploymentMergeRequest(command.configLocator(),
                                                deploymentMergeRequest.id());
                        }
                }

                return new Deployment(
                                new DeploymentId(UUID.randomUUID()),
                                command.service(),
                                command.appSourceLocator(),
                                command.environment(),
                                currentVersion,
                                command.targetVersion(),
                                changeSummary,
                                null,
                                deploymentMergeRequest);
        }

}
