package ggs.srr.service.reservation.exception;

public class NotFoundReservationException extends RuntimeException{
    public NotFoundReservationException(String message) {
        super(message);
    }
}
