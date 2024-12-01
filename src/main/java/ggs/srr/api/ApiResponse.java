package ggs.srr.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {

    private HttpStatus status;
    private int code;
    private String message;
    private T data;

}
