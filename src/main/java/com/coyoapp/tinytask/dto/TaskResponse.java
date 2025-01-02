package com.coyoapp.tinytask.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

  /** Unique identifier of the task. */
  @Schema(
      description = "Unique identifier of the task (UUID format)",
      example = "123e4567-e89b-12d3-a456-426614174000")
  private String id;

  /** Name of the task. */
  @Schema(description = "Name of the task", example = "Test tiny task")
  private String name;

  /** Due date of the task. */
  @Schema(description = "Due date of the task", type = "string", example = "2025-02-01T13:00:00")
  private LocalDateTime dueDate;
}
