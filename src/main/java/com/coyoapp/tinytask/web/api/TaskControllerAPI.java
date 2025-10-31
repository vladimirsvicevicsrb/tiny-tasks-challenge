package com.coyoapp.tinytask.web.api;

import com.coyoapp.tinytask.dto.TaskFileResponse;
import com.coyoapp.tinytask.dto.TaskRequest;
import com.coyoapp.tinytask.dto.TaskResponse;
import com.coyoapp.tinytask.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import java.util.Set;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

@OpenAPIDefinition(
    info =
        @Info(
            title = "Tiny Task Management API",
            version = "v1",
            description = "API for managing tasks."))
public interface TaskControllerAPI {

  @Operation(
      summary = "Create a new task",
      description = "Creates a new task with the provided name and due date.")
  @ApiResponse(responseCode = "201", description = "Task created successfully")
  @ApiResponse(
      responseCode = "400",
      description = "Invalid task request",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class),
              examples = {
                @ExampleObject(
                    value =
                        """
                     {
                         "timestamp": "2025-01-01T10:51:04.397761Z",
                         "message": "Validation failed",
                         "details": "One or more fields are invalid",
                         "fieldErrors": {
                             "dueDate": "must be a future date",
                             "name": "must not be empty"
                         }
                     }
                     """)
              }))
  ResponseEntity<TaskResponse> createTask(TaskRequest taskRequest, List<MultipartFile> files);

  @Operation(summary = "Get All Tasks", description = "Retrieves a list of all existing tasks.")
  @ApiResponse(responseCode = "200", description = "List of tasks retrieved successfully")
  ResponseEntity<List<TaskResponse>> getTasks();

  @Operation(
      summary = "Delete a Task",
      description = "Deletes the task with the specified ID.",
      responses = {
        @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
        @ApiResponse(
            responseCode = "404",
            description = "Task not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = {
                      @ExampleObject(
                          value =
                              """
                            {
                                "timestamp": "2025-01-01T11:21:42.426105Z",
                                "message": "Task with id 0493d5b4-d2fa-4e5d-b4cb-2a7a0bbd3a58 not found",
                                "details": "uri=/tasks/0493d5b4-d2fa-4e5d-b4cb-2a7a0bbd3a58",
                                "fieldErrors": null
                            }
                            """)
                    }))
      })
  @Parameter(name = "taskId", description = "ID of the task to be deleted")
  ResponseEntity<Void> deleteTask(String taskId);

  @Operation(
      summary = "Get Files for Task",
      description = "Retrieves a list of files for a specific task ID.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "List of files for task retrieved successfully"),
        @ApiResponse(
            responseCode = "404",
            description = "Task not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = {
                      @ExampleObject(
                          value =
                              """
                            {
                                "timestamp": "2025-01-01T11:21:42.426105Z",
                                "message": "Task with id 0493d5b4-d2fa-4e5d-b4cb-2a7a0bbd3a58 not found",
                                "details": "uri=/tasks/0493d5b4-d2fa-4e5d-b4cb-2a7a0bbd3a58",
                                "fieldErrors": null
                            }
                            """)
                    }))
      })
  @Parameter(name = "taskId", description = "ID of the task to retrieve files for")
  ResponseEntity<Set<TaskFileResponse>> getFilesForTask(@PathVariable String taskId);

  @Operation(
      summary = "Download a file",
      description = "Downloads a file by its ID.",
      responses = {
        @ApiResponse(responseCode = "200", description = "File downloaded successfully"),
        @ApiResponse(
            responseCode = "404",
            description = "File not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = {
                      @ExampleObject(
                          value =
                              """
                          {
                              "timestamp": "2025-01-01T11:21:42.426105Z",
                              "message": "File with id 0493d5b4-d2fa-4e5d-b4cb-2a7a0bbd3a58 not found",
                              "details": "uri=/files/0493d5b4-d2fa-4e5d-b4cb-2a7a0bbd3a58",
                              "fieldErrors": null
                          }
                          """)
                    }))
      })
  @Parameter(name = "fileId", description = "ID of the file to be downloaded")
  ResponseEntity<InputStreamResource> downloadFile(@PathVariable String fileId);

  @Operation(
      summary = "Delete a file",
      description = "Deletes a file by its ID.",
      responses = {
        @ApiResponse(responseCode = "204", description = "File deleted successfully"),
        @ApiResponse(
            responseCode = "404",
            description = "File not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = {
                      @ExampleObject(
                          value =
                              """
                          {
                              "timestamp": "2025-01-01T11:21:42.426105Z",
                              "message": "File with id 0493d5b4-d2fa-4e5d-b4cb-2a7a0bbd3a58 not found",
                              "details": "uri=/files/0493d5b4-d2fa-4e5d-b4cb-2a7a0bbd3a58",
                              "fieldErrors": null
                          }
                          """)
                    }))
      })
  @Parameter(name = "fileId", description = "ID of the file to be deleted")
  ResponseEntity<Void> deleteFile(@PathVariable String fileId);

  @Operation(
      summary = "Toggle task completion status",
      description =
          "Toggles the completion status of a task. If completed, marks as incomplete and vice versa.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Task completion status toggled successfully"),
        @ApiResponse(
            responseCode = "404",
            description = "Task not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = {
                      @ExampleObject(
                          value =
                              """
                          {
                              "timestamp": "2025-01-01T11:21:42.426105Z",
                              "message": "Task with id 0493d5b4-d2fa-4e5d-b4cb-2a7a0bbd3a58 not found",
                              "details": "uri=/tasks/0493d5b4-d2fa-4e5d-b4cb-2a7a0bbd3a58/toggle-completion",
                              "fieldErrors": null
                          }
                          """)
                    }))
      })
  @Parameter(name = "taskId", description = "ID of the task to toggle completion status")
  ResponseEntity<TaskResponse> toggleTaskCompletion(@PathVariable String taskId);
}
