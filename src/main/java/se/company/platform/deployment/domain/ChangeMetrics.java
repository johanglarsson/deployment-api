package se.company.platform.deployment.domain;

public record ChangeMetrics(int commitCount,
                int filesChanged,
                int totalLinesAdded,
                int totalLinesDeleted,
                boolean breakingChangePresent) {

}
