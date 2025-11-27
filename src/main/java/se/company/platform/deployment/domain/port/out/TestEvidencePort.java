package se.company.platform.deployment.domain.port.out;

import se.company.platform.deployment.domain.ServiceIdentifier;
import se.company.platform.deployment.domain.TestEvidence;
import se.company.platform.deployment.domain.Version;

public interface TestEvidencePort {

    TestEvidence getTestEvidence(ServiceIdentifier service, Version targetVersion);
}
