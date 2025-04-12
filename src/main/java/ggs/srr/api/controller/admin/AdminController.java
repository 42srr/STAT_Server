package ggs.srr.api.controller.admin;

import ggs.srr.api.controller.admin.request.InitializeUserRequest;
import ggs.srr.service.admin.AdminService;
import ggs.srr.service.client.APIClient;
import ggs.srr.service.client.dto.UserContent;
import ggs.srr.service.client.dto.UserDto;
import ggs.srr.service.client.dto.UsersRequest;
import ggs.srr.service.user.UserService;
import ggs.srr.service.user.response.UserFtIdAndIntraIdResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static ggs.srr.service.client.dto.UserProjectResponse.UsersProjectsResponse;

@RestController
@Slf4j
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final APIClient apiClient;
    private final UserService userService;

    @PutMapping("/users/init")
    public ResponseEntity<Void> initUsers(@RequestBody InitializeUserRequest request) {

        String code = request.getCode();
        log.debug("code = {}", code);

        List<UserDto> userDtos = apiClient.fetchUsersFromTurbofetch(code);
        adminService.saveUserInformation(userDtos);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/projects/init")
    public ResponseEntity<Void> initProjects() {
        List<String> projectNames = apiClient.fetchProjectsFromFetchProject();
        adminService.saveProjectsInformation(projectNames);

        return ResponseEntity.ok(null);
    }

    @PutMapping("/project-users/init")
    public ResponseEntity<Void> initUserProjects() {

        adminService.deleteAllUserProjects();
        List<UserFtIdAndIntraIdResponse> allUsersFtIdAndIntraId = userService.getAllUsersFtIdAndIntraId();
        List<UsersProjectsResponse> usersProjectsResponses = apiClient.fetchUserProjectsFromFetchProject(toUsersRequest(allUsersFtIdAndIntraId));
        adminService.initializeAllProjectUsers(usersProjectsResponses);

        return ResponseEntity.ok(null);
    }

    private UsersRequest toUsersRequest(List<UserFtIdAndIntraIdResponse> allUsersFtIdAndIntraId) {
        List<UserContent> userContents = allUsersFtIdAndIntraId.stream()
                .map(ftIdAndIntraId ->
                        new UserContent(ftIdAndIntraId.getIntraId(), ftIdAndIntraId.getFtServerId() + "")
                ).toList();

        return new UsersRequest(userContents);
    }
}
