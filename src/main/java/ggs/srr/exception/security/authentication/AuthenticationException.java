package ggs.srr.exception.security.authentication;

public class AuthenticationException extends RuntimeException {

    private AuthenticationErrorCode authenticationErrorCode;

    public AuthenticationException(AuthenticationErrorCode authenticationErrorCode) {
        super(authenticationErrorCode.getMessage());
    }

}
