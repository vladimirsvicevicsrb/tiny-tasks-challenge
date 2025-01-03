package com.coyoapp.tinytask.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

import com.coyoapp.tinytask.domain.Task;
import com.coyoapp.tinytask.dto.TaskRequest;
import com.coyoapp.tinytask.exception.TaskNotFoundException;
import com.coyoapp.tinytask.repository.TaskRepository;
import com.coyoapp.tinytask.service.mapper.TaskMapper;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

  @Mock private TaskRepository taskRepository;

  private final TaskMapper taskMapper = new TaskMapper();

  private TaskService taskService;

  @BeforeEach
  public void setUp() {
    taskService = new TaskService(taskRepository, taskMapper);
  }

  @Test
  public void testCreateTask_success() {
    // given
    var taskId = UUID.randomUUID().toString();
    var taskName = "Test Task";
    var dueDate = LocalDateTime.now().plusDays(1);

    var taskRequest = TaskRequest.builder().name(taskName).dueDate(dueDate).build();

    var savedTask = new Task();
    savedTask.setId(taskId);
    savedTask.setName(taskName);
    savedTask.setDueDate(dueDate);

    when(taskRepository.save(
            argThat(task -> task.getName().equals(taskName) && task.getDueDate().equals(dueDate))))
        .thenReturn(savedTask);

    // when
    var response = taskService.createTask(taskRequest, null);

    // then
    assertEquals(taskId, response.getId());
    assertEquals(taskName, response.getName());
    assertEquals(dueDate, response.getDueDate());
  }

  @Test
  public void testDeleteTask_notFound() {
    // given
    var taskId = UUID.randomUUID().toString();

    when(taskRepository.existsById(taskId)).thenReturn(false);

    // when
    var taskNotFoundException =
        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(taskId));

    // then
    assertEquals("Task with id %s not found".formatted(taskId), taskNotFoundException.getMessage());
  }
}
