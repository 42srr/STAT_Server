package ggs.srr.api.controller.level;

import ggs.srr.api.ApiResponse;
import ggs.srr.service.user.LevelResponse;
import ggs.srr.service.user.LevelResponseList;
import ggs.srr.service.user.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LevelController {

    private final UserService userService;

    @GetMapping("/levels")
    public ApiResponse<LevelResponseList> levelUserCounts() {
        return ApiResponse.ok(userService.getLevelInfo());
    }
}
