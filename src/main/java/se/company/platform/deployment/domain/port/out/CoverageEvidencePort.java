package se.company.platform.deployment.domain.port.out;

import se.company.platform.deployment.domain.CoverageEvidence;
import se.company.platform.deployment.domain.ServiceIdentifier;
import se.company.platform.deployment.domain.Version;

public interface CoverageEvidencePort {

    CoverageEvidence getCoverageEvidence(ServiceIdentifier service, Version targetVersion);
}
