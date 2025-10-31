package com.coyoapp.tinytask.web;

import com.coyoapp.tinytask.domain.TaskFile;
import com.coyoapp.tinytask.dto.TaskFileResponse;
import com.coyoapp.tinytask.dto.TaskRequest;
import com.coyoapp.tinytask.dto.TaskResponse;
import com.coyoapp.tinytask.logging.Timed;
import com.coyoapp.tinytask.service.TaskFileService;
import com.coyoapp.tinytask.service.TaskService;
import com.coyoapp.tinytask.web.api.TaskControllerAPI;
import jakarta.validation.Valid;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Timed
@RestController
@RequiredArgsConstructor
class TaskController implements TaskControllerAPI {

  private final TaskService taskService;
  private final TaskFileService taskFileService;

  /**
   * Creates a new task.
   *
   * @param taskRequest The task request object containing name and due date.
   * @param files The files to be attached to the task.
   * @return The created task response object.
   */
  @Override
  @PostMapping(value = "/tasks", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<TaskResponse> createTask(
      @RequestPart(name = "taskRequest") @Valid TaskRequest taskRequest,
      @RequestPart(value = "files", required = false) List<MultipartFile> files) {
    log.debug("createTask(createTask={})", taskRequest);
    final TaskResponse task = taskService.createTask(taskRequest, files);
    return ResponseEntity.status(201).body(task);
  }

  /**
   * Retrieves a list of all tasks.
   *
   * @return A list of task response objects.
   */
  @Override
  @GetMapping("/tasks")
  public ResponseEntity<List<TaskResponse>> getTasks() {
    log.debug("getTasks()");
    final List<TaskResponse> tasks = taskService.getTasks();
    return ResponseEntity.ok(tasks);
  }

  /**
   * Deletes a task by its ID.
   *
   * @param taskId The ID of the task to be deleted.
   */
  @Override
  @DeleteMapping(path = "/tasks/{taskId}")
  public ResponseEntity<Void> deleteTask(@PathVariable String taskId) {
    log.debug("deleteTask(taskId={})", taskId);
    taskService.deleteTask(taskId);
    return ResponseEntity.noContent().build();
  }

  /**
   * Retrieves a list of files for a specific task.
   *
   * @param taskId The ID of the task for which files are retrieved.
   * @return A list of files associated with the task.
   */
  @Override
  @GetMapping("/tasks/{taskId}/files")
  public ResponseEntity<Set<TaskFileResponse>> getFilesForTask(@PathVariable String taskId) {
    log.debug("getFilesForTask(taskId={})", taskId);
    return ResponseEntity.ok(taskFileService.getFilesByTaskId(taskId));
  }

  /**
   * Downloads a file by its ID.
   *
   * @param fileId The ID of the file to be downloaded.
   * @return The downloaded file as an InputStreamResource.
   */
  @Override
  @GetMapping("files/{fileId}/download")
  public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String fileId) {
    log.debug("downloadFile(fileId={})", fileId);

    final TaskFile taskFile = taskFileService.getFileById(fileId);

    final HttpHeaders headers = new HttpHeaders();
    headers.add(
        HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + taskFile.getFileName() + "\"");
    headers.add(HttpHeaders.CONTENT_TYPE, taskFile.getFileType());

    return ResponseEntity.status(HttpStatus.OK)
        .headers(headers)
        .body(new InputStreamResource(new ByteArrayInputStream(taskFile.getFileContent())));
  }

  /**
   * Deletes a file by its ID.
   *
   * @param fileId The ID of the file to be deleted.
   */
  @Override
  @DeleteMapping("files/{fileId}")
  public ResponseEntity<Void> deleteFile(@PathVariable String fileId) {
    log.debug("deleteFile(fileId={})", fileId);
    taskFileService.deleteFile(fileId);
    return ResponseEntity.noContent().build();
  }

  /**
   * Toggles the completion status of a task.
   *
   * @param taskId The ID of the task to toggle completion status.
   * @return The updated task response object.
   */
  @Override
  @PostMapping("tasks/{taskId}/toggle-completion")
  public ResponseEntity<TaskResponse> toggleTaskCompletion(@PathVariable String taskId) {
    log.debug("toggleTaskCompletion(taskId={})", taskId);
    final TaskResponse task = taskService.toggleTaskCompletion(taskId);
    return ResponseEntity.ok(task);
  }
}
