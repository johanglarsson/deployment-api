package se.company.platform.deployment.domain;

public record CommitRange(Version fromExclusive, Version toInclusive) {
}
