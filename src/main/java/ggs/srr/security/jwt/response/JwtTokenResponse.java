package ggs.srr.security.jwt.response;

import lombok.Getter;

@Getter
public class JwtTokenResponse {

    private String accessToken;
    private String refreshToken;
    private Long expire;

    public JwtTokenResponse(String accessToken, String refreshToken, Long expire) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expire = expire;
    }
}
