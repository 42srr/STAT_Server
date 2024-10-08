package ggs.srr.controller.user;

import ggs.srr.domain.user.FtUser;
import ggs.srr.service.user.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<FtUser> getUsers() {
        return userService.findAll();
    }

}
