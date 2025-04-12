package ggs.srr.exception.security.authentication;

import lombok.Getter;

@Getter
public enum AuthenticationErrorCode {

    INVALID_AUTHORIZATION_ERR("올바르지 않은 Authorization code 입니다."),
    EXPIRED_JWT_ERR("만료된 JWT Token 입니다."),
    JWT_ERR("jwt token 검증시 오류가 발생했습니다."),
    INVALID_TOKEN_FORMAT_ERR("token 형식이 잘못되었습니다.")
    ;

    private String message;

    AuthenticationErrorCode(String message) {
        this.message = message;
    }

}
