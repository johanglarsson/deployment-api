package se.company.platform.deployment.domain;

import java.time.Instant;

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
