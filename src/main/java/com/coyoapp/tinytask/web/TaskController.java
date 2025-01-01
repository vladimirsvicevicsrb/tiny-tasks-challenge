package com.coyoapp.tinytask.web;

import com.coyoapp.tinytask.dto.TaskRequest;
import com.coyoapp.tinytask.dto.TaskResponse;
import com.coyoapp.tinytask.service.TaskService;
import com.coyoapp.tinytask.web.api.TaskControllerAPI;
import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
class TaskController implements TaskControllerAPI {

  private final TaskService taskService;

  /**
   * Creates a new task.
   *
   * @param taskRequest The task request object containing name and due date.
   * @return The created task response object.
   */
  @Override
  @PostMapping
  public ResponseEntity<TaskResponse> createTask(@RequestBody @Valid TaskRequest taskRequest) {
    log.debug("createTask(createTask={})", taskRequest);
    final TaskResponse task = taskService.createTask(taskRequest);
    return ResponseEntity.status(201).body(task);
  }

  /**
   * Retrieves a list of all tasks.
   *
   * @return A list of task response objects.
   */
  @Override
  @GetMapping
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
  @DeleteMapping(path = "/{taskId}")
  public ResponseEntity<Void> deleteTask(@PathVariable String taskId) {
    log.debug("deleteTask(taskId={})", taskId);
    taskService.deleteTask(taskId);
    return ResponseEntity.noContent().build();
  }
}
