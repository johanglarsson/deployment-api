package se.company.platform.deployment.integrations;

import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.MergeRequestApi;
import org.gitlab4j.api.NotesApi;
import org.gitlab4j.api.RepositoryApi;
import org.gitlab4j.api.RepositoryFileApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IntegrationConfig {

    @Bean
    public GitLabApi gitLabApi(String hostUrl, String accessToken) {
        return new GitLabApi(hostUrl, accessToken);
    }

    @Bean
    public RepositoryApi repositoryApi(GitLabApi gitLabApi) {
        return gitLabApi.getRepositoryApi();
    }

    @Bean
    MergeRequestApi mergeRequestApi(GitLabApi gitLabApi) {
        return gitLabApi.getMergeRequestApi();
    }

    @Bean
    NotesApi notesApi(GitLabApi gitLabApi) {
        return gitLabApi.getNotesApi();
    }

    RepositoryFileApi repositoryFileApi(GitLabApi gitLabApi) {
        return gitLabApi.getRepositoryFileApi();
    }
}
