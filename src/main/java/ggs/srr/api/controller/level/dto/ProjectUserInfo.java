package ggs.srr.api.controller.level.dto;

import lombok.Data;

@Data
public class ProjectUserInfo {

    private String status;
    private String projectName;

    public ProjectUserInfo(String status, String projectName) {
        this.status = status;
        this.projectName = projectName;
    }
}
