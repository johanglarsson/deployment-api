package se.company.platform.deployment.domain.port.out;

import se.company.platform.deployment.domain.CoverageEvidence;
import se.company.platform.deployment.domain.ProjectLocator;
import se.company.platform.deployment.domain.TestEvidence;
import se.company.platform.deployment.domain.Version;

public interface CiEvidencePort {

    TestEvidence getTestEvidence(ProjectLocator locator, Version version);

    CoverageEvidence getCoverageEvidence(ProjectLocator locator, Version version);
}
