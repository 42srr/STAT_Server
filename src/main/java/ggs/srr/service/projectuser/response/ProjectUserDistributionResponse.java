package ggs.srr.service.projectuser.response;

import ggs.srr.domain.projectuser.ProjectUserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Map;

@Getter
@Schema(description = "과제 인원 분포")
public class ProjectUserDistributionResponse {

    @Schema(description = "key : 과제명, value : {key : 과제 상태, value : 인원수}")
    private Map<String, Map<ProjectUserStatus, Long>> distribution;

    public ProjectUserDistributionResponse(Map<String, Map<ProjectUserStatus, Long>> distribution) {
        this.distribution = distribution;
    }
}
