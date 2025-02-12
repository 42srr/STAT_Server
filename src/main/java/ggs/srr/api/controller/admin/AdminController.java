package ggs.srr.api.controller.admin;

import ggs.srr.api.ApiResponse;
import ggs.srr.security.AuthenticationHolder;
import ggs.srr.security.authentication.Authentication;
import ggs.srr.service.system.APIClient;
import ggs.srr.service.system.InitDataManager;
import ggs.srr.service.system.dto.UserDto;
import ggs.srr.service.system.dto.UserProjectResponse.UsersProjectsResponse;
import ggs.srr.service.system.dto.UsersRequest;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminController {

    private final InitDataManager initDataManager;
    private final AuthenticationHolder authenticationHolder;
    private final APIClient apiClient;

    @GetMapping("/admin/init/users")
    public ApiResponse<String> initUsers(HttpServletRequest request) {
        Authentication authentication = authenticationHolder.getAuthentication();
        String intraId = authentication.getIntraId();
        initDataManager.initUser(intraId);
        return ApiResponse.ok(null);
    }

    @GetMapping("/admin/init/project_users")
    public ApiResponse<String> initProjectUsers(HttpServletRequest request) {
        Authentication authentication = authenticationHolder.getAuthentication();
        String intraId = authentication.getIntraId();
        initDataManager.initProjectUser(intraId);
        return ApiResponse.ok(null);
    }

    @PostMapping("/admin/projects-user")
    public ApiResponse<List<UsersProjectsResponse>> saveAllUserProjects(@RequestBody UsersRequest usersRequest) {
        /*try {
            List<UsersProjectsResponse> userProjects = apiClient.fetchUserProjectsFromFetchProject(usersRequest);
            log.info("Fetched {} projects", userProjects.size());
            return ApiResponse.ok(userProjects);
        } catch (Exception e) {
            log.error("Error fetching user projects: {}", e.getMessage());
            return ApiResponse.badRequest(e, "failed sorry");
        }*/
        String intraId = usersRequest.getUserContents().get(0).getIntraId();
        try {
            initDataManager.initProjectUser(intraId);
            return ApiResponse.ok(null);
        } catch (Exception e) {
            log.error("Error processing user projects: {}", e.getMessage());
            return ApiResponse.badRequest(e, "Fail sorry sorry");
        }
    }

    @GetMapping("/admin/users")
    public ApiResponse<List<UserDto>> fetchAllUsers() {
        //List<UserDto> users = apiClient.fetchUsersFromTurbofetch();
        Authentication authentication = authenticationHolder.getAuthentication();
        String intraId = authentication.getIntraId();
        initDataManager.initUser(intraId);
        return ApiResponse.ok(null);
    }

    @GetMapping("/admin/projects")
    public ApiResponse<List<String>> fetchProjects() {
        initDataManager.initProjects();
        return ApiResponse.ok(null);
    }
}
