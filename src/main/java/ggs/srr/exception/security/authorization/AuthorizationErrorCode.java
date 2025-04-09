package ggs.srr.exception.security.authorization;

import lombok.Getter;

@Getter
public enum AuthorizationErrorCode {

    UNAUTHORIZED("접근 권한이 없습니다.");

    private String message;

    AuthorizationErrorCode(String message) {
        this.message = message;
    }

}
