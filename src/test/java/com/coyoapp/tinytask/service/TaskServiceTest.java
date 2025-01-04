package com.coyoapp.tinytask.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.coyoapp.tinytask.domain.Task;
import com.coyoapp.tinytask.dto.TaskRequest;
import com.coyoapp.tinytask.exception.TaskNotFoundException;
import com.coyoapp.tinytask.repository.TaskRepository;
import com.coyoapp.tinytask.service.mapper.TaskFileMapper;
import com.coyoapp.tinytask.service.mapper.TaskMapper;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;

class TaskServiceTest {

  private final TaskRepository taskRepository = mock(TaskRepository.class);
  private final TaskFileService taskFileService = mock(TaskFileService.class);
  private final TaskMapper taskMapper = new TaskMapper(mock(TaskFileMapper.class));

  private final TaskService taskService =
      new TaskService(taskRepository, taskMapper, taskFileService);

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

    final ArgumentMatcher<Task> taskMatcher =
        task -> task.getName().equals(taskName) && task.getDueDate().equals(dueDate);
    when(taskFileService.uploadFiles(argThat(taskMatcher), isNull())).thenReturn(savedTask);
    when(taskRepository.save(argThat(taskMatcher))).thenReturn(savedTask);

    // when
    var response = taskService.createTask(taskRequest, null);

    // then
    assertEquals(taskId, response.getId());
    assertEquals(taskName, response.getName());
    assertEquals(dueDate, response.getDueDate());
    assertThat(response.getFiles()).isEmpty();
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
