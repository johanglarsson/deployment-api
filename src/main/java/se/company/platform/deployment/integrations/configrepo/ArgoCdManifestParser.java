package se.company.platform.deployment.integrations.configrepo;

import se.company.platform.deployment.domain.Version;

public interface ArgoCdManifestParser {

    /**
     * Reads manifest and extracts the image version currently used.
     */
    Version readCurrentVersion(String manifest);

    /**
     * Updates the manifest with the new image version.
     */
    String updateVersion(String manifest, Version newVersion);
}