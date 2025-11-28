package se.company.platform.deployment.domain.service;

import java.util.List;

import io.pebbletemplates.pebble.PebbleEngine;
import se.company.platform.deployment.domain.ChangeClassification;
import se.company.platform.deployment.domain.ChangeMetrics;
import se.company.platform.deployment.domain.ChangeSummary;
import se.company.platform.deployment.domain.CommitRange;
import se.company.platform.deployment.domain.CommitSummary;
import se.company.platform.deployment.domain.CoverageEvidence;
import se.company.platform.deployment.domain.ProjectLocator;
import se.company.platform.deployment.domain.TestEvidence;
import se.company.platform.deployment.domain.port.out.AppSourceRepositoryPort;
import se.company.platform.deployment.domain.port.out.CiEvidencePort;

class ChangeSummaryService {

    private final CiEvidencePort ciEvidencePort;
    private final AppSourceRepositoryPort appSourceRepositoryPort;

    ChangeSummaryService(CiEvidencePort ciEvidencePort, AppSourceRepositoryPort appSourceRepositoryPort) {
        this.ciEvidencePort = ciEvidencePort;
        this.appSourceRepositoryPort = appSourceRepositoryPort;
    }

    ChangeSummary collect(CommitRange commitRange, ProjectLocator locator) {

        List<CommitSummary> commits = appSourceRepositoryPort.getCommitsBetween(commitRange, locator);
        CoverageEvidence coverageEvidence = ciEvidencePort.getCoverageEvidence(locator, commitRange.toInclusive());
        TestEvidence testEvidence = ciEvidencePort.getTestEvidence(locator, commitRange.toInclusive());
        return new ChangeSummary(
                commitRange,
                commits,
                ChangeMetrics.fromCommits(commits),
                testEvidence,
                coverageEvidence,
                ChangeClassification.MINOR);

    }

}
