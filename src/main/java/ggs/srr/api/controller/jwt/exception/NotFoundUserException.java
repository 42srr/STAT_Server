package ggs.srr.api.controller.jwt.exception;

public class NotFoundUserException extends JwtControllerException {

    public NotFoundUserException() {
        super();
    }

    public NotFoundUserException(String message) {
        super(message);
    }

    public NotFoundUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundUserException(Throwable cause) {
        super(cause);
    }

    protected NotFoundUserException(String message, Throwable cause, boolean enableSuppression,
                                    boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
