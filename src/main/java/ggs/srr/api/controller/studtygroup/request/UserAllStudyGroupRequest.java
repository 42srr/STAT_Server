package ggs.srr.api.controller.studtygroup.request;

import ggs.srr.service.reservation.studygroup.request.UserAllStudyGroupServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserAllStudyGroupRequest {

    @NotNull(message = "사용자 id 값은 필수 입니다.")
    private Long userId;

    public UserAllStudyGroupRequest(Long userId) {
        this.userId = userId;
    }

    public UserAllStudyGroupServiceRequest toServiceRequest() {
        return new UserAllStudyGroupServiceRequest(userId);
    }
}
