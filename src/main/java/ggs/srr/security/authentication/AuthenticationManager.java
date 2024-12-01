package ggs.srr.security.authentication;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationManager {

    private String grantType;
    private String name;
    private String clientId;
    private String clientSecret;
    private String redirectURL;
    private String authorizationUri;
    private String userInfoUri;
    private String tokenUri;
    private Set<String> scope;


    public String getAuthorizationUri() {
        System.out.println("client id = " + clientId);
        return this.authorizationUri + "?client_id=" + clientId + "&redirect_uri=" + getEncodedRedirectUri()
                + "&response_type=code";
    }

    private String getEncodedRedirectUri() {
        return URLEncoder.encode(this.redirectURL, StandardCharsets.UTF_8);
    }
}
