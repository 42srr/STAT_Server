package ggs.srr.exception.security.authentication;

public class AuthenticationException extends RuntimeException {

    private ErrorCode errorCode;

    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
