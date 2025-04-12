package ggs.srr.exception.projectuser;

import org.springframework.http.HttpStatus;

public class ProjectUserException extends RuntimeException {

  private HttpStatus httpStatus;

  public ProjectUserException(ProjectUserErrorCode errorCode) {
    super(errorCode.getMessage());
    this.httpStatus = errorCode.getHttpStatus();
  }
}
