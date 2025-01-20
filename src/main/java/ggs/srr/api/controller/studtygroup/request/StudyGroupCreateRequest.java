package ggs.srr.api.controller.studtygroup.request;

import ggs.srr.service.studygroup.request.StudyGroupCreateServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class StudyGroupCreateRequest {

    @NotNull(message = "사용자 id list 는 필수 입니다.")
    private List<Long> userIds;

    @NotBlank(message = "스터디 그룹 이름은 필수 입니다.")
    private String groupName;

    public StudyGroupCreateRequest(List<Long> userIds, String groupName) {
        this.userIds = userIds;
        this.groupName = groupName;
    }

    public StudyGroupCreateServiceRequest toServiceRequest() {
        return new StudyGroupCreateServiceRequest(userIds, groupName);
    }

}
