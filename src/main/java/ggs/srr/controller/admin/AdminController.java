package ggs.srr.controller.admin;

import ggs.srr.service.initdata.InitDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AdminController {

    private final InitDataService initDataService;

    @GetMapping("/admin/test")
    public String test() {
        return "admin user";
    }

    @GetMapping("/admin/init_data")
    public String init(@RegisteredOAuth2AuthorizedClient("42") OAuth2AuthorizedClient client) throws InterruptedException {
        initDataService.initUserAndProjectData(client);
        return "ok";
    }

}
