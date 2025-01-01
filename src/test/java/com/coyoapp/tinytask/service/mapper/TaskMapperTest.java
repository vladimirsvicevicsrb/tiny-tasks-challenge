package com.coyoapp.tinytask.service.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.coyoapp.tinytask.domain.Task;
import com.coyoapp.tinytask.dto.TaskRequest;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class TaskMapperTest {

  private final TaskMapper taskMapper = new TaskMapper();

  @Test
  public void testToTask_shouldMapRequestToTask() {
    // given
    var request =
        TaskRequest.builder().name("Test Task").dueDate(LocalDateTime.now().plusDays(1)).build();

    // when
    var task = taskMapper.toTask(request);

    // then
    assertNotNull(task);
    assertEquals(request.getName(), task.getName());
    assertEquals(request.getDueDate(), task.getDueDate());
  }

  @Test
  public void testToResponse_shouldMapTaskToResponse() {
    // given
    var task = new Task();
    task.setId(UUID.randomUUID().toString());
    task.setName("Test Task");
    task.setDueDate(LocalDateTime.now().plusDays(1));

    // when
    var response = taskMapper.toResponse(task);

    // then
    assertNotNull(response);
    assertEquals(task.getId(), response.getId());
    assertEquals(task.getName(), response.getName());
    assertEquals(task.getDueDate(), response.getDueDate());
  }
}
