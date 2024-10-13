package ggs.srr.config;

import ggs.srr.oauth.client.Client;
import ggs.srr.oauth.client.ClientBuilder;
import ggs.srr.oauth.client.ClientManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class OAuth2Config {

    @Value("${42.client-id}")
    private String clientId;

    @Value("${42.client-secret}")
    private String clientSecret;

    @Value("${42.redirect-uri}")
    private String redirectURI;

    @Bean
    public ClientManager clientManager(){
        ClientManager clientManager = new ClientManager();
        Client client42 = new ClientBuilder()
                            .grantType("authorization_code")
                            .name("42")
                            .clientId(clientId)
                            .clientSecret(clientSecret)
                            .redirectURL(redirectURI)
                            .authorizationUri("https://api.intra.42.fr/oauth/authorize")
                            .tokenUri("https://api.intra.42.fr/oauth/token")
                            .userInfoUri("https://api.intra.42.fr/v2/me")
                            .scope( "profile", "public", "projects", "elearning", "tig", "forum")
                            .build();
        clientManager.addClient(client42);
        return clientManager;
    }
}
