package ggs.srr.api.controller.project;

import ggs.srr.api.ApiResponse;
import ggs.srr.service.projectuser.ProjectUserService;
import ggs.srr.service.projectuser.response.ProjectUserDistributionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectUserService projectUserService;

    @GetMapping("/distribution")
    public ApiResponse<ProjectUserDistributionResponse> getProjectsDistribution() {
        ProjectUserDistributionResponse projectUserDistribution = projectUserService.getProjectUserDistribution();
        return ApiResponse.ok(projectUserDistribution);
    }

}
