package se.company.platform.deployment.domain;

import java.net.URI;

public record MergeRequestInfo(
                String id,
                URI url,
                String branchName) {
}