package ggs.srr.api.controller.admin;

import ggs.srr.service.system.InitDataManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AdminController {

    private static final int TOKEN_INDEX = 1;
    private static final String DELIMITER = " ";

    private final InitDataManager initDataManager;

    @Autowired
    public AdminController(InitDataManager initDataManager) {
        this.initDataManager = initDataManager;
    }

    @GetMapping("/admin/init_data")
    public String initData(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        String token = getJwtToken(authorization);
        initDataManager.initData(token);
        return "init user data";
    }

    private static String getJwtToken(String authorization) {
        return authorization.split(DELIMITER)[TOKEN_INDEX];
    }
}
