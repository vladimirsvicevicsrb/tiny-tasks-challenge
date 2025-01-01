package com.coyoapp.tinytask.dto;

import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
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
  @Schema(description = "Name of the task", example = "Test tiny task", requiredMode = Schema.RequiredMode.REQUIRED)
  private String name;

  /**
   * Due date of the task.
   * Must be a future date/time.
   */
  @Future
  @Schema(description = "Due date of the task", type = "string", example = "2025-02-01T13:00:00")
  private LocalDateTime dueDate;

}
