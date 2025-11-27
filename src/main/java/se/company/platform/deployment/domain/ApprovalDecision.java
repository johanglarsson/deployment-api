package se.company.platform.deployment.domain;

public record ApprovalDecision(
                ApprovalOutcome outcome,
                String rationale) {
}
