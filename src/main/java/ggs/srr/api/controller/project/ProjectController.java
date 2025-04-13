package ggs.srr.api.controller.project;

import ggs.srr.api.ApiResponse;
import ggs.srr.service.projectuser.ProjectUserService;
import ggs.srr.service.projectuser.response.ProjectUserDistributionResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "프로젝트 API - BETA", description = "42 과제에 대한 API 목록입니다.")
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
