package ggs.srr.service.user.request;

import lombok.Getter;

@Getter
public class UserUpdateServiceRequest {
    private final String oAuth2AccessToken;
    private final String oAuth2RefreshToken;
    private final Long targetFtServerId;

    public UserUpdateServiceRequest(String oAuth2AccessToken, String oAuth2RefreshToken, Long targetFtServerId) {
        this.oAuth2AccessToken = oAuth2AccessToken;
        this.oAuth2RefreshToken = oAuth2RefreshToken;
        this.targetFtServerId = targetFtServerId;
    }
}
