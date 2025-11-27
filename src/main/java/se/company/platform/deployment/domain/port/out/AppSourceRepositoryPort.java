package se.company.platform.deployment.domain.port.out;

import java.util.List;

import se.company.platform.deployment.domain.CommitRange;
import se.company.platform.deployment.domain.CommitSummary;
import se.company.platform.deployment.domain.ServiceLocator;

public interface AppSourceRepositoryPort {

    List<CommitSummary> getCommitsBetween(CommitRange range, ServiceLocator locator);

}