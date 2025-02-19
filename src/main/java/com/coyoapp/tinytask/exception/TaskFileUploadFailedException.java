package com.coyoapp.tinytask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TaskFileUploadFailedException extends RuntimeException {

  public TaskFileUploadFailedException(String message, Throwable cause) {
    super(message, cause);
  }
}
