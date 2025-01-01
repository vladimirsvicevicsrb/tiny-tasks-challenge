package com.coyoapp.tinytask.service;

import com.coyoapp.tinytask.domain.Task;
import com.coyoapp.tinytask.dto.TaskRequest;
import com.coyoapp.tinytask.dto.TaskResponse;
import com.coyoapp.tinytask.exception.TaskNotFoundException;
import com.coyoapp.tinytask.repository.TaskRepository;
import com.coyoapp.tinytask.service.mapper.TaskMapper;
import jakarta.validation.Valid;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

  private final TaskRepository taskRepository;
  private final TaskMapper taskMapper;

  public TaskResponse createTask(@Valid TaskRequest taskRequest) {
    log.debug("creating task: {}", taskRequest);
    final Task taskToSave = taskMapper.toTask(taskRequest);
    final Task savedTask = taskRepository.save(taskToSave);
    log.debug("created task: {}", savedTask);

    return taskMapper.toResponse(savedTask);
  }

  public List<TaskResponse> getTasks() {
    return taskRepository.findAll()
        .stream()
        .map(taskMapper::toResponse)
        .sorted(Comparator.comparing(TaskResponse::getDueDate, Comparator.nullsLast(Comparator.naturalOrder()))
            .thenComparing(TaskResponse::getName))
        .toList();
  }

  public void deleteTask(String taskId) {
    if (!taskRepository.existsById(taskId)) {
      throw new TaskNotFoundException("Task with id %s not found".formatted(taskId));
    }

    taskRepository.deleteById(taskId);
  }
}
