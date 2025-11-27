package se.company.platform.deployment.domain.service;

import java.util.List;
import java.util.UUID;

import se.company.platform.deployment.domain.ChangeMetrics;
import se.company.platform.deployment.domain.CommitRange;
import se.company.platform.deployment.domain.CommitSummary;
import se.company.platform.deployment.domain.CoverageEvidence;
import se.company.platform.deployment.domain.Deployment;
import se.company.platform.deployment.domain.DeploymentId;
import se.company.platform.deployment.domain.MergeRequestInfo;
import se.company.platform.deployment.domain.TestEvidence;
import se.company.platform.deployment.domain.Version;
import se.company.platform.deployment.domain.port.in.DeploymentUseCase;
import se.company.platform.deployment.domain.port.out.CommitEvidencePort;
import se.company.platform.deployment.domain.port.out.ConfigRepositoryPort;
import se.company.platform.deployment.domain.port.out.CoverageEvidencePort;
import se.company.platform.deployment.domain.port.out.TestEvidencePort;
import se.company.platform.deployment.domain.port.out.VersionControlPort;

public final class DeploymentService implements DeploymentUseCase {

        private final ConfigRepositoryPort configRepository;
        private final CommitEvidencePort commitHistory;
        private final TestEvidencePort testEvidencePort;
        private final CoverageEvidencePort coverageEvidencePort;
        private final VersionControlPort versionControlPort;
        private final ClassificationPolicy classificationPolicy;

        public DeploymentService(
                        ConfigRepositoryPort configRepository,
                        CommitEvidencePort commitHistory,
                        TestEvidencePort testEvidencePort,
                        CoverageEvidencePort coverageEvidencePort,
                        VersionControlPort versionControlPort,
                        ClassificationPolicy classificationPolicy) {
                this.configRepository = configRepository;
                this.commitHistory = commitHistory;
                this.testEvidencePort = testEvidencePort;
                this.coverageEvidencePort = coverageEvidencePort;
                this.versionControlPort = versionControlPort;
                this.classificationPolicy = classificationPolicy;
        }

        @Override
        public Deployment deploy(DeployCommand command) {
                Version previousVersion = configRepository.getCurrentVersion(command.service(), command.environment());

                CommitRange range = new CommitRange(previousVersion, command.targetVersion());

                List<CommitSummary> commits = commitHistory.getCommitsBetween(range, command.service());
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
