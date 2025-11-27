package se.company.platform.deployment.domain.service;

import static java.util.Objects.requireNonNull;

import java.util.List;

import se.company.platform.deployment.domain.CommitRange;
import se.company.platform.deployment.domain.CommitSummary;
import se.company.platform.deployment.domain.Deployment;
import se.company.platform.deployment.domain.Version;
import se.company.platform.deployment.domain.port.in.DeploymentUseCase;
import se.company.platform.deployment.domain.port.out.GitOpsConfigRepositoryPort;
import se.company.platform.deployment.domain.port.out.CiEvidencePort;
import se.company.platform.deployment.domain.port.out.AppSourceRepositoryPort;

public final class DeploymentService implements DeploymentUseCase {

        private final GitOpsConfigRepositoryPort gitOpsConfigRepositoryPort;
        private final CiEvidencePort ciEvidencePort;
        private final AppSourceRepositoryPort appSourceRepositoryPort;

        public DeploymentService(GitOpsConfigRepositoryPort gitOpsConfigRepositoryPort, CiEvidencePort ciEvidencePort,
                        AppSourceRepositoryPort appSourceRepositoryPort) {
                this.gitOpsConfigRepositoryPort = requireNonNull(gitOpsConfigRepositoryPort);
                this.ciEvidencePort = requireNonNull(ciEvidencePort);
                this.appSourceRepositoryPort = requireNonNull(appSourceRepositoryPort);
        }

        @Override
        public Deployment deploy(DeployCommand command) {
                Version currentVersion = gitOpsConfigRepositoryPort.getDeployedVersion(command.configLocator(),
                                command.service(),
                                command.environment());

                CommitRange range = new CommitRange(currentVersion, command.targetVersion());

                List<CommitSummary> commits = appSourceRepositoryPort.getCommitsBetween(range,
                                command.appSourceLocator());
                /**
                 * ChangeMetrics metrics = buildMetrics(commits);
                 * TestEvidence tests = testEvidencePort.getTestEvidence(command.service(),
                 * command.targetVersion());
                 * CoverageEvidence coverage =
                 * coverageEvidencePort.getCoverageEvidence(command.service(),
                 * command.targetVersion());
                 * 
                 * ChangeClassification classification = classificationPolicy.classify(metrics,
                 * tests, coverage);
                 * 
                 * ChangeSummary changeSummary = new ChangeSummary(range, commits, metrics,
                 * tests, coverage,
                 * classification);
                 * 
                 * String branchName = createBranchName(command, previousVersion);
                 * String mrTitle = "Deploy " + command.service().value() + " " +
                 * command.targetVersion().value() + " to " + command.environment();
                 * 
                 * String initialDescription = renderInitialMrDescription(changeSummary);
                 * 
                 * MergeRequestInfo mr = configRepository.updateVersionAndOpenMergeRequest(
                 * command.service(),
                 * command.environment(),
                 * previousVersion,
                 * command.targetVersion(),
                 * branchName,
                 * mrTitle,
                 * initialDescription);
                 * 
                 * ApprovalDecision decision = decideApproval(command.environment(),
                 * classification, changeSummary);
                 * 
                 * applyDecisionToMergeRequest(mr.id(), decision);
                 * 
                 * return new Deployment(
                 * new DeploymentId(UUID.randomUUID()),
                 * command.service(),
                 * command.environment(),
                 * previousVersion,
                 * command.targetVersion(),
                 * changeSummary,
                 * decision,
                 * mr);
                 **/
                return null;
        }

        // helper methods: buildMetrics, createBranchName, renderInitialMrDescription,
        // decideApproval, applyDecisionToMergeRequest
}
