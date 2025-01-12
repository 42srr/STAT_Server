package ggs.srr.api;

import ggs.srr.api.controller.jwt.exception.JwtControllerException;
import ggs.srr.repository.reservation.exception.FindIdNullException;
import ggs.srr.service.studygroup.exception.EmptyUserIdException;
import ggs.srr.service.studygroup.exception.NoSuchUserException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JwtControllerException.class)
    public ApiResponse<Object> jwtControllerException(JwtControllerException e) {
        return ApiResponse.badRequest(e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResponse<Object> bindException(BindException e) {
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ApiResponse.badRequest(e, message);
    }

    // study room service exception handlers

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmptyUserIdException.class)
    public ApiResponse<Object> emptyUserIdException(EmptyUserIdException e) {
        String message = e.getMessage();
        return ApiResponse.badRequest(e, message);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoSuchUserException.class)
    public ApiResponse<Object> noSuchUserException(NoSuchUserException e) {
        return ApiResponse.badRequest(e, e.getMessage());
    }

    // study room service exception handlers
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FindIdNullException.class)
    public ApiResponse<Object> findByNull(FindIdNullException e) {
        String message = e.getMessage();
        return ApiResponse.badRequest(e, message);
    }
}
