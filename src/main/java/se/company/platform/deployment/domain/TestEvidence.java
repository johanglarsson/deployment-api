package se.company.platform.deployment.domain;

import java.net.URI;

public record TestEvidence(int totalTests,
                int failedTests,
                boolean unitTestPresent,
                boolean integrationTestPresent,
                URI testReportURL) {

}
