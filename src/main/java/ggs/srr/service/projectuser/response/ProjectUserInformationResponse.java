package ggs.srr.service.projectuser.response;

import ggs.srr.domain.projectuser.ProjectUser;
import ggs.srr.domain.projectuser.ProjectUserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "사용자가 진행중인 과제 응답")
public class ProjectUserInformationResponse {

    @Schema(description = "과제 고유 id")
    private Long projectId;

    @Schema(description = "과제 이름")
    private String projectName;

    @Schema(description = "점수")
    private int finalMark;

    @Schema(description = "상태")
    private ProjectUserStatus status;

    public ProjectUserInformationResponse(ProjectUser projectUser) {
        projectId = projectUser.getProject().getId();
        projectName = projectUser.getProject().getName();
        finalMark = projectUser.getFinalMark();
        status = projectUser.getStatus();
    }
}
