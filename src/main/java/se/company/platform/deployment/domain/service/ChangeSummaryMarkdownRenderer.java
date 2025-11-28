package se.company.platform.deployment.domain.service;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.template.PebbleTemplate;
import se.company.platform.deployment.domain.ChangeSummary;

public class ChangeSummaryMarkdownRenderer {

    private final PebbleEngine engine;

    public ChangeSummaryMarkdownRenderer() {
        this.engine = new PebbleEngine.Builder()
                .autoEscaping(false) // Markdown → disable HTML escaping
                .build();
    }

    public String render(ChangeSummary summary) {
        try {
            PebbleTemplate template = engine.getTemplate("templates/change-summary.md.peb");

            Map<String, Object> ctx = new HashMap<>();
            ctx.put("classification", summary.classification());
            ctx.put("commitRange", summary.commitRange());
            ctx.put("commits", summary.commits());
            ctx.put("metrics", summary.metrics());
            ctx.put("testEvidence", summary.testEvidence());
            ctx.put("coverageEvidence", summary.coverageEvidence());

            StringWriter writer = new StringWriter();
            template.evaluate(writer, ctx);
            return writer.toString();

        } catch (Exception e) {
            throw new RuntimeException("Failed to render ChangeSummary markdown", e);
        }
    }
}
