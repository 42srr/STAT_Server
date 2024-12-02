package ggs.srr.filter.dto;

import lombok.Data;

@Data
public class LoginInfoDto {

    private String intraId;
    private String accessToken;
    private String refreshToken;

    public LoginInfoDto(String intraId, String accessToken, String refreshToken) {
        this.intraId = intraId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
