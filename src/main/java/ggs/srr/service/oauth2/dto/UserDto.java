package ggs.srr.service.oauth2.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter @Setter
public class UserDto {

    private FtResponse ftResponse;
    private OAuth2User oAuth2User;

    private String role;

    public UserDto(FtResponse ftResponse, OAuth2User oAuth2User, String role) {
        this.ftResponse = ftResponse;
        this.oAuth2User = oAuth2User;
        this.role = role;
    }
}
