package com.coyoapp.tinytask.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.coyoapp.tinytask.domain.Task;
import com.coyoapp.tinytask.domain.TaskFile;
import com.coyoapp.tinytask.dto.TaskFileResponse;
import com.coyoapp.tinytask.dto.TaskRequest;
import com.coyoapp.tinytask.dto.TaskResponse;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class TaskMapperTest {

  private final TaskFileMapper taskFileMapper = new TaskFileMapper();
  private final TaskMapper taskMapper = new TaskMapper(taskFileMapper);

  @Test
  void testToTask_shouldMapRequestToTask() {
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
  void testToResponse_shouldMapTaskToResponse() {
    // given
    final Task task = createTaskWithFiles();

    // when
    final TaskResponse response = taskMapper.toResponse(task);

    // then
    assertTaskResponse(response, task);
  }

  private Task createTask() {
    Task task = new Task();
    task.setId(UUID.randomUUID().toString());
    task.setName("Test Task");
    task.setDueDate(LocalDateTime.now().plusDays(1));
    return task;
  }

  private Task createTaskWithFiles() {
    Task task = createTask();
    task.setFiles(Set.of(createTaskFile(task, "file1.txt", "text/plain", 11)));
    return task;
  }

  private TaskFile createTaskFile(Task task, String fileName, String fileType, long fileSize) {
    TaskFile file = new TaskFile();
    file.setId(UUID.randomUUID().toString());
    file.setTask(task);
    file.setFileName(fileName);
    file.setFileType(fileType);
    file.setFileSize(fileSize);
    return file;
  }

  private void assertTaskResponse(TaskResponse response, Task task) {
    assertNotNull(response);
    assertEquals(task.getId(), response.getId());
    assertEquals(task.getName(), response.getName());
    assertEquals(task.getDueDate(), response.getDueDate());
    assertEquals(task.getFiles().size(), response.getFiles().size());
    assertThat(task.getFiles()).hasSize(1);

    TaskFile givenTaskFile = task.getFiles().iterator().next();
    TaskFileResponse expectedTaskFile = response.getFiles().iterator().next();
    assertThat(givenTaskFile.getId()).isNotNull();
    assertThat(givenTaskFile.getTask().getId()).isEqualTo(expectedTaskFile.getTaskId());
    assertThat(givenTaskFile.getFileName()).isEqualTo(expectedTaskFile.getFileName());
    assertThat(givenTaskFile.getFileType()).isEqualTo(expectedTaskFile.getFileType());
    assertThat(givenTaskFile.getFileSize()).isEqualTo(expectedTaskFile.getFileSize());
  }
}
