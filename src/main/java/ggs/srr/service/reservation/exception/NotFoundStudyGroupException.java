package ggs.srr.service.reservation.exception;

public class NotFoundStudyGroupException extends RuntimeException{
    public NotFoundStudyGroupException(String message) {
        super(message);
    }
}
