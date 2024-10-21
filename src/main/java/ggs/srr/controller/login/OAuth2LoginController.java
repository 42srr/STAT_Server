package ggs.srr.controller.login;

import ggs.srr.oauth.client.Client;
import ggs.srr.oauth.client.ClientManager;
import ggs.srr.oauth.provider.ProviderManager;
import ggs.srr.oauth.provider.dto.JwtToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class OAuth2LoginController {

    private final ClientManager clientManager;
    private final ProviderManager providerManager;

    @Autowired
    public OAuth2LoginController(ClientManager clientManager, ProviderManager providerManager) {
        this.clientManager = clientManager;
        this.providerManager = providerManager;
    }

    @GetMapping("/login")
    public LoginInfoDto login(@RequestParam(name = "code")String code){
        log.info("login controller 호출");
        Client client = clientManager.getClient("42");
        return providerManager.attemptAuthentication(code, client);
    }
}
