package ggs.srr.service.projectuser.request;

import ggs.srr.domain.projectuser.ProjectUserStatus;
import lombok.Getter;

@Getter
public class ProjectUsersRequest {
    private Long userId;
    private ProjectUserStatus status;

    public ProjectUsersRequest(Long userId, ProjectUserStatus status) {
        this.userId = userId;
        this.status = status;
    }
}
