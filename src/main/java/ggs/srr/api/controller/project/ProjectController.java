package ggs.srr.api.controller.project;

import ggs.srr.api.controller.main.dto.ProjectUserInfoDto;
import ggs.srr.api.controller.main.dto.ProjectsDto;
import ggs.srr.domain.project.Project;
import ggs.srr.domain.projectuser.ProjectUser;
import ggs.srr.domain.user.FtUser;
import ggs.srr.repository.project.ProjectRepository;
import ggs.srr.service.user.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final UserService userService;

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

    @GetMapping("/projects/{intraId}")
    public List<ProjectUserInfoDto> projectUserInfo(@PathVariable String intraId) {
        Optional<FtUser> ftUser = userService.findByIntraId(intraId);
        List<ProjectUser> projectUsers = ftUser.get().getProjectUsers();
        List<ProjectUserInfoDto> res = new ArrayList<>();
        for(ProjectUser projectUser : projectUsers) {
            res.add(new ProjectUserInfoDto(projectUser.getStatus().getText(), projectUser.getProject().getName()));
        }
        return res;
    }
}
