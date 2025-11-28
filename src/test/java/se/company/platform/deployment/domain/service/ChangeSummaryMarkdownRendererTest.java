package se.company.platform.deployment.domain.service;

import java.net.URI;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;

import se.company.platform.deployment.domain.ChangeClassification;
import se.company.platform.deployment.domain.ChangeMetrics;
import se.company.platform.deployment.domain.ChangeSummary;
import se.company.platform.deployment.domain.CommitRange;
import se.company.platform.deployment.domain.CommitSummary;
import se.company.platform.deployment.domain.CoverageEvidence;
import se.company.platform.deployment.domain.TestEvidence;
import se.company.platform.deployment.domain.Version;

public class ChangeSummaryMarkdownRendererTest {

    @Test
    void givenChangeSummary_whenRender_shouldProduceMarkdown() {
        ChangeSummaryMarkdownRenderer subject = new ChangeSummaryMarkdownRenderer();

        ChangeSummary summary = new ChangeSummary(
                new CommitRange(new Version("1.0.0"), new Version("2.0.0")),
                List.of(createCommitSummary()),
                new ChangeMetrics(1, 0, 0, 0, false),
                new TestEvidence(0, 0, true, true, URI.create("http://localhost")),
                new CoverageEvidence(0.0, 0.0, 0.0, URI.create("http://localhost")),
                ChangeClassification.BIG

        );
        String result = subject.render(summary);

        System.out.println(result);
    }

    private CommitSummary createCommitSummary() {
        return new CommitSummary(
                "123",
                "My commit",
                "luke@skywalker",
                Instant.now(),
                0,
                0,
                0,
                null);
    }
}
