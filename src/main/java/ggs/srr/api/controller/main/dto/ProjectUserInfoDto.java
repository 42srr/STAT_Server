package ggs.srr.api.controller.main.dto;

import lombok.Data;

@Data
public class ProjectUserInfoDto {

    private String status;
    private String projectName;

    public ProjectUserInfoDto(String status, String projectName) {
        this.status = status;
        this.projectName = projectName;
    }
}
