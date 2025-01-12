package ggs.srr.service.studygroup.request;

import lombok.Data;

import java.util.List;

@Data
public class StudyGroupCreateServiceRequest {

    private List<Long> userIds;
    private String groupName;

    public StudyGroupCreateServiceRequest(List<Long> userIds, String groupName) {
        this.userIds = userIds;
        this.groupName = groupName;
    }
}
