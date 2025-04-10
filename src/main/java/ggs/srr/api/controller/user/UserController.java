package ggs.srr.api.controller.user;

import ggs.srr.api.controller.ApiResponse;
import ggs.srr.domain.projectuser.ProjectUserStatus;
import ggs.srr.service.projectuser.ProjectUserService;
import ggs.srr.service.projectuser.request.ProjectUserRequest;
import ggs.srr.service.projectuser.request.ProjectUsersRequest;
import ggs.srr.service.projectuser.response.ProjectUserInformationResponse;
import ggs.srr.service.user.UserService;
import ggs.srr.service.user.request.UserInformationServiceRequest;
import ggs.srr.service.user.request.UserRankingServiceRequest;
import ggs.srr.service.user.response.LevelDistributionResponse;
import ggs.srr.service.user.response.UserInformationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ProjectUserService projectUserService;

    @GetMapping("/{userId}")
    public ApiResponse<UserInformationResponse> getUserInformation(@PathVariable Long userId) {
        UserInformationResponse response = userService.findById(new UserInformationServiceRequest(userId), LocalDateTime.now());
        return ApiResponse.ok(response);
    }

    @GetMapping("/distribution/level")
    public ApiResponse<LevelDistributionResponse> getLevelDistribution() {
        LevelDistributionResponse userLevelDistribution = userService.getUserLevelDistribution();
        return ApiResponse.ok(userLevelDistribution);
    }

    @GetMapping("/ranking")
    public ApiResponse<List<UserInformationResponse>> getRanking(
            @RequestParam(value = "type", defaultValue = "level") String type,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {

        List<UserInformationResponse> result = null;
        UserRankingServiceRequest request = new UserRankingServiceRequest(page, size);

        if (type.equals("correction-point")) {
            result = userService.getUserRankingOfCollectionPoint(request);
        } else if (type.equals("wallet")) {
            result = userService.getUserRankingOfWallet(request);
        } else {
            result = userService.getUserRankingOfLevel(request);
        }

        return ApiResponse.ok(result);
    }

    @GetMapping("/{userId}/projects")
    public ApiResponse<List<ProjectUserInformationResponse>> getUserProjects(
            @PathVariable Long userId,
            @RequestParam(value = "status", defaultValue = "default") String statusText
    ) {
        ProjectUserStatus status = ProjectUserStatus.getByText(statusText);
        List<ProjectUserInformationResponse> userProjects = projectUserService.getUserProjects(new ProjectUsersRequest(userId, status));
        return ApiResponse.ok(userProjects);
    }

    @GetMapping("/{userId}/projects/{projectId}")
    public ApiResponse<ProjectUserInformationResponse> getUserProject(
            @PathVariable Long userId,
            @PathVariable Long projectId
    ) {

        ProjectUserInformationResponse userProject = projectUserService.getUserProject(new ProjectUserRequest(userId, projectId));
        return ApiResponse.ok(userProject);
    }
}
