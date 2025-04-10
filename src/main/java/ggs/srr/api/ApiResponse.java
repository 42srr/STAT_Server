package ggs.srr.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {

    private int code;
    private HttpStatus status;
    private T data;

    private ApiResponse(HttpStatus status,  T data) {
        this.code = status.value();
        this.status = status;
        this.data = data;
    }

    public static <T> ApiResponse<T> of(HttpStatus httpStatus, T data) {
        return new ApiResponse<>(httpStatus, data);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.of(HttpStatus.OK, data);
    }

    public static <T> ApiResponse<T> badRequest(T data) {
        return ApiResponse.of(HttpStatus.BAD_REQUEST, data);
    }

}
