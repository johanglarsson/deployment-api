package se.company.platform.deployment.domain;

import java.net.URI;

public record CoverageEvidence(double lineCoverage, double branchCoverage, Double coverageDelta,
        URI coverageReportURI) {

}
