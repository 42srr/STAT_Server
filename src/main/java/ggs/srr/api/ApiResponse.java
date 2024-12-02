package ggs.srr.api;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {

    private static final String SUCCESS_MESSAGE = "Success";

    private HttpStatus status;
    private int code;
    private String message;
    private T data;

    private ApiResponse(HttpStatus status, int code, String message, T data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(HttpStatus.OK, HttpStatus.OK.value(), SUCCESS_MESSAGE, data);
    }

    public static <T> ApiResponse<T> badRequest(Exception e) {
        return new ApiResponse<>(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
    }

}
