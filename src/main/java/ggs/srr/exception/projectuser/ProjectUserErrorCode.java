package ggs.srr.exception.projectuser;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ProjectUserErrorCode {

    NOT_FOUND_USER_PROJECT("해당 사용자의 프로젝트를 조회할 수 없습니다.", HttpStatus.BAD_REQUEST);

    private String message;
    private HttpStatus httpStatus;

    ProjectUserErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
