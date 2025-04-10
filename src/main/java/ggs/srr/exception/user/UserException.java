package ggs.srr.exception.user;

import org.springframework.http.HttpStatus;

public class UserException extends RuntimeException {

  private HttpStatus httpStatus;

  public UserException(UserErrorCode errorCode) {
    super(errorCode.getMessage());
    this.httpStatus = errorCode.getHttpStatus();
  }

}
