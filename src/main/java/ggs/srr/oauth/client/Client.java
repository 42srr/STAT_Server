package ggs.srr.oauth.client;


import java.net.URI;
import java.util.Set;
import lombok.Getter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.web.util.UriComponentsBuilder;

@Getter
public class Client {

    private String grantType;
    private String name;
    private String clientId;
    private String clientSecret;
    private String redirectURL;
    private String authorizationUri;
    private String userInfoUri;
    private String tokenUri;
    private Set<String> scope;

    public Client(String grantType, String name, String clientId, String clientSecret, String redirectURL,
                  String authorizationUri, String tokenUri, String userInfoUri ,Set<String> scope) {
        this.grantType = grantType;
        this.name = name;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectURL = redirectURL;
        this.authorizationUri = authorizationUri;
        this.tokenUri = tokenUri;
        this.userInfoUri = userInfoUri;
        this.scope = scope;
    }

}
