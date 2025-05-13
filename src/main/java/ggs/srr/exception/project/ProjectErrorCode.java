package ggs.srr.exception.project;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ProjectErrorCode {
    NOT_FOUND_PROJECT("해당 프로젝트를 조회할 수 없습니다.", HttpStatus.BAD_REQUEST);

    private String message;
    private HttpStatus httpStatus;

    ProjectErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
