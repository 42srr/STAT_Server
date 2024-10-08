package ggs.srr.oauth.auth.dto;

import ggs.srr.domain.project.Project;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectStatusDto {
    
    private Project project;
    private String status;
}
