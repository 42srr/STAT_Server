package ggs.srr.security.authentication.response;

import lombok.Getter;

@Getter
public class AuthorizationServerResponse {

    private String accessToken;
    private String refreshToken;

    public AuthorizationServerResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
