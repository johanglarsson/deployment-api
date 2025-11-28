package se.company.platform.deployment.domain;

import java.util.List;

public record ChangeMetrics(int commitCount,
        int filesChanged,
        int totalLinesAdded,
        int totalLinesDeleted,
        boolean breakingChangePresent) {

    public static ChangeMetrics fromCommits(List<CommitSummary> commitSummaries) {
        int linesAdded = 0;
        int linesDeleted = 0;
        int filesChanged = 0;
        boolean breakingChangePresent = false;
        for (CommitSummary commitSummary : commitSummaries) {
            linesAdded = linesAdded + commitSummary.linesAdded();
            linesDeleted = linesDeleted + commitSummary.linesDeleted();
            filesChanged = filesChanged + commitSummary.filesChanged();
            if (commitSummary.breakingChangeInfo().breaking()) {
                breakingChangePresent = true;
            }
        }
        return new ChangeMetrics(commitSummaries.size(), filesChanged, linesAdded, linesDeleted, breakingChangePresent);
    }

}
