package ggs.srr.api.controller.user;

import ggs.srr.api.ApiResponse;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "사용자 API", description = "사용자 정보, 랭킹, 사용자가 진행중인 과제에 대한 REST API 목록 입니다.")
@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ProjectUserService projectUserService;

    @Operation(summary = "사용자 개인의 정보", description = "고유 아이디, 인트라 아이디, 레벨, 알타리안 달러, 평가 포인트, 사진 url, 업데이트 가능한지 에 대한 정보를 담고 있습니다.")
    @Parameter(name = "userId", description = "사용자 고유 아이디")
    @GetMapping("/{userId}")
    public ApiResponse<UserInformationResponse> getUserInformation(@PathVariable Long userId) {
        UserInformationResponse response = userService.findById(new UserInformationServiceRequest(userId), LocalDateTime.now());
        return ApiResponse.ok(response);
    }

    @Operation(summary = "사용자 레벨 분포", description = "사용자들의 레벨 분포를 반환합니다.")
    @GetMapping("/distribution/level")
    public ApiResponse<LevelDistributionResponse> getLevelDistribution() {
        LevelDistributionResponse userLevelDistribution = userService.getUserLevelDistribution();
        return ApiResponse.ok(userLevelDistribution);
    }

    @Operation(summary = "사용자 랭킹", description = "쿼리 파라미터에 대한 사용자의 랭킹을 반환합니다.")
    @Parameter(name = "type", description = "타입에 따른 사용자를 랭킹순으로 반환합니다. \ntype : level, correction-point, wallet")
    @Parameter(name = "page", description = "페이지를 나타냅니다.")
    @Parameter(name = "size", description = "반환받을 데이터 수를 의미합니다.")
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

    @Operation(summary = "사용자의 과제", description = "사용자가 완료하거나 진행중인 과제를 반환합니다.")
    @Parameter(name = "userId", description = "사용자 고유 id 를 의미합니다.")
    @Parameter(name ="status", description = "프로젝트 상태를 통해 필터링할 수 있습니다. \ndefault : 모든 사용자의 과제 반환\nfinished : 완료한 과제 반환\nin_progress : 진행중인 과제 반환")
    @GetMapping("/{userId}/projects")
    public ApiResponse<List<ProjectUserInformationResponse>> getUserProjects(
            @PathVariable Long userId,
            @RequestParam(value = "status", defaultValue = "default") String statusText
    ) {
        ProjectUserStatus status = ProjectUserStatus.getByText(statusText);
        List<ProjectUserInformationResponse> userProjects = projectUserService.getUserProjects(new ProjectUsersRequest(userId, status));
        return ApiResponse.ok(userProjects);
    }

    @Operation(summary = "사용자의 특정 과제", description = "사용자의 특정 과제를 반환합니다.")
    @Parameter(name = "userId", description = "사용자 고유 id 를 의미합니다.")
    @Parameter(name = "projectId", description = "과제의 고유 id 를 의미합니다..")
    @GetMapping("/{userId}/projects/{projectId}")
    public ApiResponse<ProjectUserInformationResponse> getUserProject(
            @PathVariable Long userId,
            @PathVariable Long projectId
    ) {

        ProjectUserInformationResponse userProject = projectUserService.getUserProject(new ProjectUserRequest(userId, projectId));
        return ApiResponse.ok(userProject);
    }
}
