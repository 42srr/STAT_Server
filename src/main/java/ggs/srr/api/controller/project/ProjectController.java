package ggs.srr.api.controller.project;

import ggs.srr.api.controller.main.dto.ProjectsDto;
import ggs.srr.domain.project.Project;
import ggs.srr.domain.projectuser.ProjectUser;
import ggs.srr.repository.project.ProjectRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectRepository projectRepository;

    @GetMapping("/projects")
    public List<ProjectsDto> projectInfo() {
        List<ProjectsDto> res = new ArrayList<>();
        List<Project> projects = projectRepository.findAll();
        for (Project project : projects) {
            int count = 0;
            String projectName = project.getName();
            List<ProjectUser> projectUsers = project.getProjectUsers();
            for (ProjectUser projectUser : projectUsers) {
                if (projectUser.getStatus().equals("in_progress")){
                    count++;
                }
            }
            ProjectsDto projectsDto = new ProjectsDto(project.getName(), count);
            res.add(projectsDto);
        }
        return res;
    }
}
