package ggs.srr.service.projectuser.response;

import ggs.srr.domain.projectuser.ProjectUser;
import ggs.srr.domain.projectuser.ProjectUserStatus;
import lombok.Getter;

@Getter
public class ProjectUserInformationResponse {

    private Long projectUserId;
    private String projectName;
    private int finalMark;
    private ProjectUserStatus status;

    public ProjectUserInformationResponse(ProjectUser projectUser) {
        projectUserId = projectUser.getId();
        projectName = projectUser.getProject().getName();
        finalMark = projectUser.getFinalMark();
        status = projectUser.getStatus();
    }
}
