package se.company.platform.deployment.domain.port.out;

import java.util.List;

import se.company.platform.deployment.domain.CommitRange;
import se.company.platform.deployment.domain.CommitSummary;
import se.company.platform.deployment.domain.ServiceIdentifier;

public interface CommitEvidencePort {
    List<CommitSummary> getCommitsBetween(CommitRange range, ServiceIdentifier service);
}
