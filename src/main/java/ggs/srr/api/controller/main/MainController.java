package ggs.srr.api.controller.main;

import ggs.srr.api.ApiResponse;
import ggs.srr.api.controller.main.dto.LevelDto;
import ggs.srr.service.user.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;

    @GetMapping("/levels")
    public ApiResponse<LevelDto> levelUserCounts() {
        return ApiResponse.ok(userService.getLevelInfo());
    }

}
