package ggs.srr.security.authentication.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthenticationResponse {

    private String tokenType;
    private String intraId;
    private String accessToken;
    private String refreshToken;
    private Long expire;

    @Builder
    private AuthenticationResponse(String tokenType, String intraId, String accessToken, String refreshToken, Long expire) {
        this.tokenType = tokenType;
        this.intraId = intraId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expire = expire;
    }
}
