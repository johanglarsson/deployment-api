package se.company.platform.deployment.domain.port.out;

public interface VersionControlPort {

    void updateMergeRequestDescription(String mergeRequestId, String markdownBody);

    void addNoteToMergeRequest(String mergeRequestId, String note);

    void approveMergeRequest(String mergeRequestId, String approverIdentity);

    void mergeMergeRequest(String mergeRequestId);
}