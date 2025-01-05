package com.coyoapp.tinytask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TaskFileNotFoundException extends RuntimeException {

  public TaskFileNotFoundException(String message) {
    super(message);
  }
}
