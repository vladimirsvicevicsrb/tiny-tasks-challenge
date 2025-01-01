package com.coyoapp.tinytask.dto;

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

  /**
   * Unique identifier of the task.
   */
  private String id;

  /**
   * Name of the task.
   */
  private String name;

  /**
   * Due date of the task.
   */
  private LocalDateTime dueDate;

}
