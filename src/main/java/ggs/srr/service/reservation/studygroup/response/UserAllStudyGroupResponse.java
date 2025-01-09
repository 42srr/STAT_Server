package ggs.srr.service.reservation.studygroup.response;

import lombok.Data;

import java.util.List;

@Data
public class UserAllStudyGroupResponse {

    private String groupName;
    private List<UserResponse> users;
    private int size;

}
