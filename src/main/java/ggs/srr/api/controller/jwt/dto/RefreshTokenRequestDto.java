package ggs.srr.api.controller.jwt.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequestDto {

    @NotBlank(message = "refresh token 을 입력해 주세요.")
    private String refreshToken;
}
