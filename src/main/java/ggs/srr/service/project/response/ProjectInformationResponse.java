package ggs.srr.service.project.response;

import lombok.Getter;

@Getter
public class ProjectInformationResponse {

    private String projectName;

    public ProjectInformationResponse(String projectName) {
        this.projectName = projectName;
    }
}
