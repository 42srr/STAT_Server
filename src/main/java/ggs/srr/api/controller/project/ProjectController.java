package ggs.srr.api.controller.project;

import ggs.srr.api.ApiResponse;
import ggs.srr.api.controller.level.dto.ProjectUserInfo;
import ggs.srr.api.controller.level.dto.ProjectsDto;
import ggs.srr.domain.project.Project;
import ggs.srr.domain.projectuser.ProjectUser;
import ggs.srr.domain.projectuser.ProjectUserStatus;
import ggs.srr.domain.user.User;
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

    /**
     * 메서드 분리 필요
     */
    @GetMapping("/projects")
    public ApiResponse<List<ProjectsDto>> projectInfo() {
        List<ProjectsDto> res = new ArrayList<>();
        List<Project> projects = projectRepository.findAll();
        for (Project project : projects) {
            int count = 0;
            List<ProjectUser> projectUsers = project.getProjectUsers();
            for (ProjectUser projectUser : projectUsers) {
                ProjectUserStatus status = projectUser.getStatus();
                if (ProjectUserStatus.isInProgress(status)) {
                    count++;
                }
            }
            ProjectsDto projectsDto = new ProjectsDto(project.getName(), count);
            res.add(projectsDto);
        }
        return ApiResponse.ok(res);
    }

//    @GetMapping("/projects/{intraId}")
//    public ApiResponse<List<ProjectUserInfo>> projectUserInfo(@PathVariable String intraId) {
//        Optional<User> User = userService.findByIntraId(intraId);
//        List<ProjectUser> projectUsers = User.get().getProjectUsers();
//        List<ProjectUserInfo> res = new ArrayList<>();
//        for (ProjectUser projectUser : projectUsers) {
//            res.add(new ProjectUserInfo(projectUser.getStatus().getText(), projectUser.getProject().getName()));
//        }
//        return ApiResponse.ok(res);
//    }
}
