package ggs.srr.api.controller.admin;

import ggs.srr.security.AuthenticationHolder;
import ggs.srr.security.authentication.Authentication;
import ggs.srr.service.system.InitDataManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminController {

    private static final int TOKEN_INDEX = 1;
    private static final String DELIMITER = " ";

    private final InitDataManager initDataManager;
    private final AuthenticationHolder authenticationHolder;

    @GetMapping("/admin/init/users")
    public String initUsers(HttpServletRequest request) {
        Authentication authentication = authenticationHolder.getAuthentication();
        String intraId = authentication.getIntraId();
        initDataManager.initUser(intraId);
        return "init user data";
    }

    @GetMapping("/admin/init/project_users")
    public String initProjectUsers(HttpServletRequest request) {
        Authentication authentication = authenticationHolder.getAuthentication();
        String intraId = authentication.getIntraId();
        initDataManager.initProjectUser(intraId);
        return "init user data";
    }



}
