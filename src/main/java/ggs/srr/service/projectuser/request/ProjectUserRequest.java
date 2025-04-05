package ggs.srr.service.projectuser.request;

import lombok.Getter;

@Getter
public class ProjectUserRequest {

    private Long userId;
    private Long projectId;

    public ProjectUserRequest(Long userId, Long projectId) {
        this.userId = userId;
        this.projectId = projectId;
    }
}
