package ggs.srr.service.reservation.exception;

public class EmptyUserIdException extends RuntimeException {
    public EmptyUserIdException(String message) {
        super(message);
    }
}
