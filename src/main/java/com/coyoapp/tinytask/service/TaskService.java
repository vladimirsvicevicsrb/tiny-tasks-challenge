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
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

  private final TaskRepository taskRepository;
  private final TaskMapper taskMapper;
  private final TaskFileService taskFileService;

  /**
   * Creates a new task and persists it to the database.
   *
   * @param taskRequest The task request object
   * @param files The files to be attached to the task
   * @return The created task response object
   */
  public TaskResponse createTask(@Valid TaskRequest taskRequest, List<MultipartFile> files) {
    log.debug("creating task: {}", taskRequest);
    final Task taskToSave = taskMapper.toTask(taskRequest);

    Task savedTask = taskRepository.save(taskToSave);
    log.debug("created task: {}", savedTask);

    savedTask = taskFileService.uploadFiles(savedTask, files);
    log.debug("uploaded files for task: {}", savedTask);

    return taskMapper.toResponse(savedTask);
  }

  /**
   * Retrieves a list of all tasks from the database.
   *
   * @return A list of task response objects, sorted by due date (ascending, nulls last) and then by
   *     name.
   */
  public List<TaskResponse> getTasks() {
    return taskRepository.findAll().stream()
        .map(taskMapper::toResponse)
        .sorted(
            Comparator.comparing(
                    TaskResponse::getDueDate, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(TaskResponse::getName))
        .toList();
  }

  /**
   * Deletes a task from the database by its ID.
   *
   * @param taskId The ID of the task to be deleted.
   * @throws TaskNotFoundException if a task with the provided ID is not found in the database.
   */
  public void deleteTask(String taskId) {
    if (!taskRepository.existsById(taskId)) {
      throw new TaskNotFoundException("Task with id %s not found".formatted(taskId));
    }

    taskRepository.deleteById(taskId);
  }
}
