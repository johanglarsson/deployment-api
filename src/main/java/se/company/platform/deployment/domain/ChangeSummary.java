package se.company.platform.deployment.domain;

import java.util.List;

public record ChangeSummary(
                CommitRange commitRange,
                List<CommitSummary> commits,
                ChangeMetrics metrics,
                TestEvidence testEvidence,
                CoverageEvidence coverageEvidence,
                ChangeClassification classification) {
        public record BreakingChangeInfo(boolean breaking, String reason) {

        }

}
