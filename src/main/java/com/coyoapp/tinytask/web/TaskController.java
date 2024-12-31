package com.coyoapp.tinytask.web;

import com.coyoapp.tinytask.dto.TaskRequest;
import com.coyoapp.tinytask.dto.TaskResponse;
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
class TaskController {

  @PostMapping
  public ResponseEntity<TaskResponse> createTask(@RequestBody @Valid TaskRequest taskRequest) {
    log.debug("createTask(createTask={})", taskRequest);
    return ResponseEntity.ok().build();
  }

  @GetMapping
  public ResponseEntity<List<TaskResponse>> getTasks() {
    log.debug("getTasks()");
    return ResponseEntity.ok().build();
  }

  @DeleteMapping(path = "/{taskId}")
  public ResponseEntity<?> deleteTask(@PathVariable String taskId) {
    log.debug("deleteTask(taskId={})", taskId);
    return ResponseEntity.noContent().build();
  }
}
