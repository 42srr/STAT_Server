package ggs.srr.exception.service.projectuser;

public class NotFoundProjectUserException extends RuntimeException {
    public NotFoundProjectUserException(String message) {
        super(message);
    }
}
