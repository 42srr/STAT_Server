package ggs.srr.api.controller.jwt.dto;

import lombok.Data;

@Data
public class RefreshTokenResponseDTO {

    private final String accessToken;
    private final String refreshToken;

    public RefreshTokenResponseDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
