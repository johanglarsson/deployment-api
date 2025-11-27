package se.company.platform.deployment.integrations.configrepo;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

import se.company.platform.deployment.domain.Version;

public class ArgoCdYamlManifestParserTest {

    @Test
    void testGivenArgoCdApplicationManifest_whenReadCurrentVersion_ShouldReturnTargetRevision() throws IOException {

        // given
        String argocd = readTestResource("argocdApplication.yaml");
        ArgoCdManifestParser subject = new ArgoCdYamlManifestParser();

        // when
        Version actualVersion = subject.readCurrentVersion(argocd);

        // then
        Version expectedVersion = new Version("HEAD");

        assertThat(actualVersion).isEqualTo(expectedVersion);

    }

    @Test
    void testUpdateVersion() throws IOException {

        // given
        String testManifest = readTestResource("argocdApplication.yaml");
        ArgoCdManifestParser subject = new ArgoCdYamlManifestParser();
        Version newVersion = new Version("demo");
        // when
        String updatedManifest = subject.updateVersion(testManifest, newVersion);

        // then
        Version actualVersion = subject.readCurrentVersion(updatedManifest);
        assertThat(actualVersion).isEqualTo(newVersion);

    }

    private String readTestResource(String name) throws IOException {
        ClassLoader cl = getClass().getClassLoader();
        try (InputStream is = cl
                .getResourceAsStream("se/company/platform/deployment/integrations/configrepo/" + name)) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}