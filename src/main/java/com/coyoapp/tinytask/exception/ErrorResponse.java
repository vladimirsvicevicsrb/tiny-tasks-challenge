package com.coyoapp.tinytask.exception;

import java.time.Instant;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
  private Instant timestamp;
  private String message;
  private String details;
  private Map<String, String> fieldErrors;
}
