package se.company.platform.deployment.integrations.appsource;

import static java.util.Objects.requireNonNull;

import java.util.List;

import org.gitlab4j.api.CommitsApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.TagsApi;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.Tag;

import se.company.platform.deployment.domain.ChangeSummary.BreakingChangeInfo;
import se.company.platform.deployment.domain.CommitRange;
import se.company.platform.deployment.domain.CommitSummary;
import se.company.platform.deployment.domain.ProjectLocator;
import se.company.platform.deployment.domain.port.out.AppSourceRepositoryPort;

public class GitLabAppSourceRepositoryAdapter implements AppSourceRepositoryPort {

    private final CommitsApi commitsApi;
    private final TagsApi tagsApi;

    public GitLabAppSourceRepositoryAdapter(CommitsApi commitsApi, TagsApi tagsApi) {
        this.commitsApi = requireNonNull(commitsApi);
        this.tagsApi = requireNonNull(tagsApi);
    }

    @Override
    public List<CommitSummary> getCommitsBetween(CommitRange range, ProjectLocator locator) {
        try {
            Tag fromTag = tagsApi.getTag(locator.path(), range.fromExclusive().value());
            Tag toTag = tagsApi.getTag(locator.path(), range.toInclusive().value());
            return commitsApi
                    .getCommitsStream(locator.path(), fromTag.getName(), fromTag.getCreatedAt(), toTag.getCreatedAt())
                    .map(this::toDomain)
                    .toList();
        } catch (GitLabApiException e) {
            throw new IllegalStateException("Unable to get commits", e);
        }
    }

    private CommitSummary toDomain(Commit commit) {
        return new CommitSummary(
                commit.getId(),
                commit.getMessage(),
                commit.getAuthorEmail(),
                commit.getTimestamp().toInstant(),
                commit.getStats().getTotal(),
                commit.getStats().getAdditions(),
                commit.getStats().getDeletions(),
                new BreakingChangeInfo(false, "test"));
    }

}
