package se.company.platform.deployment.integrations.ci;

import se.company.platform.deployment.domain.CoverageEvidence;
import se.company.platform.deployment.domain.ProjectLocator;
import se.company.platform.deployment.domain.TestEvidence;
import se.company.platform.deployment.domain.Version;
import se.company.platform.deployment.domain.port.out.CiEvidencePort;

public class GitLabCiEvidenceAdapter implements CiEvidencePort {

    @Override
    public TestEvidence getTestEvidence(ProjectLocator locator, Version version) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTestEvidence'");
    }

    @Override
    public CoverageEvidence getCoverageEvidence(ProjectLocator locator, Version version) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCoverageEvidence'");
    }

}
