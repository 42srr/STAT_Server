package ggs.srr.oauth.auth;

import ggs.srr.domain.project.Project;
import ggs.srr.oauth.auth.dto.ProjectStatusDto;
import ggs.srr.service.project.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class FtProjectParser {

    private final ProjectService projectService;

    public FtProjectParser(ProjectService projectService) {
        this.projectService = projectService;
    }

    public List<ProjectStatusDto> parseUsersProject(ResponseEntity<HashMap> responseEntity) {
        ArrayList<ProjectStatusDto> projects = new ArrayList<>();
        ArrayList<HashMap<String, Object>> projectUsers = (ArrayList<HashMap<String, Object>>) responseEntity.getBody().get("projects_users");
        for (HashMap<String, Object> projectUser : projectUsers) {
            int cursusId = (int) ((ArrayList) projectUser.get("cursus_ids")).get(0);
            if (cursusId == 21) {
                HashMap<String, Object> project = (HashMap<String, Object>) projectUser.get("project");
                String projectName = project.get("name").toString();
                System.out.println("project = " + project);
                String status = projectUser.get("status").toString();

                if (projectService.findByProjectName(projectName).isEmpty()){
                    projectService.save(new Project(projectName));
                }
                Optional<Project> byProjectName = projectService.findByProjectName(projectName);
                if (byProjectName.isPresent()) {
                    projects.add(new ProjectStatusDto(byProjectName.get(), status));
                }
            }
        }
        return projects;
    }
}
