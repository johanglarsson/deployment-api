package se.company.platform.deployment.integrations.gitops;

import org.yaml.snakeyaml.Yaml;

import se.company.platform.deployment.domain.Version;

import org.yaml.snakeyaml.DumperOptions;

import java.io.StringWriter;
import java.util.Map;

public class ArgoCdYamlManifestParser implements ArgoCdManifestParser {

    private final Yaml yaml;

    public ArgoCdYamlManifestParser() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        this.yaml = new Yaml(options);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Version readCurrentVersion(String manifest) {
        Map<String, Object> root = yaml.load(manifest);
        Map<String, Object> spec = (Map<String, Object>) root.get("spec");
        if (spec == null) {
            throw new IllegalStateException("YAML does not contain spec");
        }
        Map<String, Object> source = (Map<String, Object>) spec.get("source");
        if (source == null) {
            throw new IllegalStateException("YAML does not contain source");
        }
        Object node = source.get("targetRevision");

        return new Version(node.toString());

    }

    @Override
    @SuppressWarnings("unchecked")
    public String updateVersion(String manifest, Version newVersion) {
        Object rootObj = yaml.load(manifest);
        if (!(rootObj instanceof Map<?, ?> root)) {
            throw new IllegalStateException("YAML root is not an object/map");
        }

        Object specObj = root.get("spec");
        if (!(specObj instanceof Map<?, ?> spec)) {
            throw new IllegalStateException("YAML does not contain object 'spec'");
        }

        Object sourceObj = spec.get("source");
        if (!(sourceObj instanceof Map)) {
            throw new IllegalStateException("YAML does not contain object 'spec.source'");
        }
        Map<String, Object> source = (Map<String, Object>) sourceObj;
        source.put("targetRevision", newVersion.value());

        // Dump back to string
        StringWriter writer = new StringWriter();
        yaml.dump(root, writer);
        return writer.toString();
    }

}
