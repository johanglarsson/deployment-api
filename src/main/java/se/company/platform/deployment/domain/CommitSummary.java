package se.company.platform.deployment.domain;

import java.time.Instant;

import se.company.platform.deployment.domain.ChangeSummary.BreakingChangeInfo;

public record CommitSummary(
                String id,
                String message,
                String author,
                Instant timestamp,
                int filesChanged,
                int linesAdded,
                int linesDeleted,
                BreakingChangeInfo breakingChangeInfo) {
}
