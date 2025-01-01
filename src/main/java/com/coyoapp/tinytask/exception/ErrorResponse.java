package com.coyoapp.tinytask.exception;

import java.time.Instant;
import java.util.Map;

public class ErrorResponse {
  private Instant timestamp;
  private String message;
  private String details;
  private Map<String, String> fieldErrors;

  public ErrorResponse(String message, String details) {
    this.timestamp = Instant.now();
    this.message = message;
    this.details = details;
  }

  public ErrorResponse(String message, String details, Map<String, String> fieldErrors) {
    this.timestamp = Instant.now();
    this.message = message;
    this.details = details;
    this.fieldErrors = fieldErrors;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Instant timestamp) {
    this.timestamp = timestamp;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getDetails() {
    return details;
  }

  public void setDetails(String details) {
    this.details = details;
  }

  public Map<String, String> getFieldErrors() {
    return fieldErrors;
  }

  public void setFieldErrors(Map<String, String> fieldErrors) {
    this.fieldErrors = fieldErrors;
  }
}
