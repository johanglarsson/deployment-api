package se.company.platform.deployment.domain.port.out;

import se.company.platform.deployment.domain.CoverageEvidence;
import se.company.platform.deployment.domain.ServiceLocator;
import se.company.platform.deployment.domain.TestEvidence;
import se.company.platform.deployment.domain.Version;

public interface CiEvidencePort {

    TestEvidence getTestEvidence(ServiceLocator locator, Version version);

    CoverageEvidence getCoverageEvidence(ServiceLocator locator, Version version);
}
