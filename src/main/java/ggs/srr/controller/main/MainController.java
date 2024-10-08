package ggs.srr.controller.main;

import ggs.srr.service.user.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;

    @GetMapping("/levels")
    public Map<Integer, Integer> levelUserCounts() { return userService.getLevelInfo(); }
}
