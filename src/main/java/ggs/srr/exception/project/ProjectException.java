package ggs.srr.exception.project;

import org.springframework.http.HttpStatus;

public class ProjectException extends RuntimeException {
  private HttpStatus httpStatus;

  public ProjectException(ProjectErrorCode errorCode) {
    super(errorCode.getMessage());
    this.httpStatus = errorCode.getHttpStatus();
  }
}
