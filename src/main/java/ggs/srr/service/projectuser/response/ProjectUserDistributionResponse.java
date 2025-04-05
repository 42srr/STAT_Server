package ggs.srr.service.projectuser.response;

import ggs.srr.domain.projectuser.ProjectUserStatus;
import lombok.Getter;

import java.util.Map;

@Getter
public class ProjectUserDistributionResponse {

    private Map<String, Map<ProjectUserStatus, Long>> distribution;

    public ProjectUserDistributionResponse(Map<String, Map<ProjectUserStatus, Long>> distribution) {
        this.distribution = distribution;
    }
}
