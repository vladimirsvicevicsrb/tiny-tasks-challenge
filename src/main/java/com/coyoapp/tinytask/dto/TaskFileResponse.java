package com.coyoapp.tinytask.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskFileResponse {

  /** Unique identifier of the file (UUID format). */
  @Schema(
      description = "Unique identifier of the file (UUID format)",
      example = "123e4567-e89b-12d3-a456-426614174000")
  private String id;

  /** Unique identifier of the task (UUID format). */
  @Schema(
      description = "Unique identifier of the task (UUID format)",
      example = "123e4567-e89b-12d3-a456-426614174000")
  private String taskId;

  /** Name of the file. */
  @Schema(description = "Name of the file", example = "Test file")
  private String fileName;

  /** Type of the file. */
  @Schema(description = "Type of the file", example = "application/pdf")
  private String fileType;

  /** Size of the file in bytes. */
  @Schema(description = "Size of the file in bytes", example = "1024")
  private Long fileSize;
}
