package ggs.srr.service.reservation.studygroup.request;

import lombok.Data;

@Data
public class UserAllStudyGroupServiceRequest {

    private Long userId;

    public UserAllStudyGroupServiceRequest(Long userId) {
        this.userId = userId;
    }
}
