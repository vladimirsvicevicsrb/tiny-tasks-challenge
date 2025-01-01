package com.coyoapp.tinytask.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {

  /**
   * Name of the task.
   * Required field.
   */
  @NotEmpty
  private String name;

  /**
   * Due date of the task.
   * Must be a future date/time.
   */
  @Future
  private LocalDateTime dueDate;

}
