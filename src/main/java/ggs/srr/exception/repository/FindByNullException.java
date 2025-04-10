package ggs.srr.exception.repository;

public class FindByNullException extends RuntimeException {
    public FindByNullException(String message) {
        super(message);
    }
}
