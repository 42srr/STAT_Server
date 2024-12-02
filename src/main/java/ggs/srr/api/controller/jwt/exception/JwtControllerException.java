package ggs.srr.api.controller.jwt.exception;

public class JwtControllerException extends RuntimeException{

    public JwtControllerException() {
        super();
    }

    public JwtControllerException(String message) {
        super(message);
    }

    public JwtControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtControllerException(Throwable cause) {
        super(cause);
    }

    protected JwtControllerException(String message, Throwable cause, boolean enableSuppression,
                                     boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
