package ggs.srr.exception.user;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserErrorCode {

    NOT_FOUND_USER("해당 사용자를 조회할 수 없습니다.", HttpStatus.BAD_REQUEST),
    UPDATE_NOT_AVAILABLE("마지막 업데이트 후 12시간이 지나야 다시 업데이트할 수 있습니다.", HttpStatus.BAD_REQUEST),
    API_FETCH_FAILED("API 접근에 실패했습니다.", HttpStatus.BAD_REQUEST),
    OAUTH_TOKEN_NOT_FOUND("인증 토큰이 없습니다.", HttpStatus.BAD_REQUEST),
    ACCESS_TOKEN_EXPIRED("인증 토큰이 만료되었습니다. 다시 로그인해주세요.", HttpStatus.BAD_REQUEST),
    ;// 확인해봐야함

    private String message;
    private HttpStatus httpStatus;

    UserErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
