package ggs.srr.api.controller.user;

import ggs.srr.api.ApiResponse;

import ggs.srr.api.controller.user.dto.UserResponse;
import ggs.srr.api.controller.user.dto.UsersResponse;
import ggs.srr.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ApiResponse<UsersResponse> getUsers() {
        List<UserResponse> users = userService.findAll();
        return ApiResponse.ok(new UsersResponse(users));
    }

    @GetMapping("/users/{intraId}")
    public ApiResponse<UserResponse> getUser(@PathVariable("intraId") String intraId) {
        return ApiResponse.ok(userService.findByIntraIdForApi(intraId));
    }

}
