package ggs.srr.exception.security.authorization;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException(AuthorizationErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
