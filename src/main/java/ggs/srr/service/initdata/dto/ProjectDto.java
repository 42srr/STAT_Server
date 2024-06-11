package ggs.srr.service.initdata.dto;

import ggs.srr.domain.Project;
import lombok.Data;

@Data
public class ProjectDto {

    public ProjectDto(String status, Project project) {
        this.status = status;
        this.project = project;
    }

    private String status;
    private Project project;
}
