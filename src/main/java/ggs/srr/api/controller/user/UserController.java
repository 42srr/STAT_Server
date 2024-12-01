package ggs.srr.api.controller.user;

import ggs.srr.api.controller.user.dto.UserDto;
import ggs.srr.service.user.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserDto> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/users/{intraId}")
    public UserDto getUser(@PathVariable("intraId")String intraId) {
        return userService.findByIntraIdForApi(intraId);
    }

}
