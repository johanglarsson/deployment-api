package se.company.platform.deployment.domain;

public enum ApprovalOutcome {
    AUTO_APPROVE_AND_MERGE,
    AUTO_APPROVE_BUT_REQUIRE_MANUAL_MERGE,
    REQUIRE_MANUAL_REVIEW
}
