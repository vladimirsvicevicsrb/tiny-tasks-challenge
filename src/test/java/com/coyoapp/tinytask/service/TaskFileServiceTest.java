package com.coyoapp.tinytask.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.coyoapp.tinytask.domain.Task;
import com.coyoapp.tinytask.domain.TaskFile;
import com.coyoapp.tinytask.dto.TaskFileResponse;
import com.coyoapp.tinytask.exception.TaskFileNotFoundException;
import com.coyoapp.tinytask.exception.TaskFileUploadFailedException;
import com.coyoapp.tinytask.exception.TaskNotFoundException;
import com.coyoapp.tinytask.repository.TaskFileRepository;
import com.coyoapp.tinytask.repository.TaskRepository;
import com.coyoapp.tinytask.service.mapper.TaskFileMapper;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

class TaskFileServiceTest {

  private final TaskRepository taskRepository = mock(TaskRepository.class);
  private final TaskFileRepository taskFileRepository = mock(TaskFileRepository.class);
  private final TaskFileMapper taskFileMapper = new TaskFileMapper();

  private final TaskFileService taskFileService =
      new TaskFileService(taskRepository, taskFileRepository, taskFileMapper);

  @Test
  void testUploadFiles_success() {
    // given
    var taskId = UUID.randomUUID().toString();
    var task = new Task();
    task.setId(taskId);

    var mockFile =
        new MockMultipartFile("file1.txt", "file1.txt", "text/plain", "sample content".getBytes());

    when(taskRepository.save(task)).thenReturn(task);

    // when
    var updatedTask = taskFileService.uploadFiles(task, List.of(mockFile));

    // then
    assertNotNull(updatedTask);
    assertEquals(1, updatedTask.getFiles().size());

    TaskFile taskFile = updatedTask.getFiles().iterator().next();
    assertEquals("file1.txt", taskFile.getFileName());
    assertEquals("text/plain", taskFile.getFileType());
    assertEquals((long) "sample content".getBytes().length, taskFile.getFileSize());
  }

  @Test
  void testUploadFiles_ioException() throws Exception {
    // given
    var taskId = UUID.randomUUID().toString();
    var task = new Task();
    task.setId(taskId);

    MultipartFile faultyFile = mock(MultipartFile.class);
    when(faultyFile.getBytes()).thenThrow(IOException.class);

    // when
    var exception =
        assertThrows(
            TaskFileUploadFailedException.class,
            () -> taskFileService.uploadFiles(task, List.of(faultyFile)));

    // then
    assertEquals("File upload for task %s failed".formatted(taskId), exception.getMessage());
  }

  @Test
  void testGetFilesByTaskId_success() {
    // given
    var taskId = UUID.randomUUID().toString();
    var task = new Task();
    task.setId(taskId);

    TaskFile file = new TaskFile();
    file.setId(UUID.randomUUID().toString());
    file.setFileName("file1.txt");
    file.setFileType("text/plain");
    file.setFileSize(1024L);
    file.setTask(task);
    task.getFiles().add(file);

    when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

    // when
    var files = taskFileService.getFilesByTaskId(taskId);

    // then
    assertNotNull(files);
    assertEquals(1, files.size());

    TaskFileResponse fileResponse = files.iterator().next();
    assertEquals(file.getId(), fileResponse.getId());
    assertEquals(taskId, fileResponse.getTaskId());
    assertEquals(file.getFileType(), fileResponse.getFileType());
    assertEquals(file.getFileSize(), fileResponse.getFileSize());
    assertEquals(file.getFileName(), fileResponse.getFileName());
  }

  @Test
  void testGetFilesByTaskId_taskNotFound() {
    // given
    var taskId = UUID.randomUUID().toString();

    when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

    // when
    var exception =
        assertThrows(TaskNotFoundException.class, () -> taskFileService.getFilesByTaskId(taskId));

    // then
    assertEquals("Task not found with id %s".formatted(taskId), exception.getMessage());
  }

  @Test
  void testGetFileById_success() {
    // given
    var fileId = UUID.randomUUID().toString();
    TaskFile taskFile = new TaskFile();
    taskFile.setId(fileId);
    taskFile.setFileName("file1.txt");

    when(taskFileRepository.findById(fileId)).thenReturn(Optional.of(taskFile));

    // when
    var retrievedFile = taskFileService.getFileById(fileId);

    // then
    assertNotNull(retrievedFile);
    assertEquals(fileId, retrievedFile.getId());
    assertEquals("file1.txt", retrievedFile.getFileName());
  }

  @Test
  void testGetFileById_fileNotFound() {
    // given
    var fileId = UUID.randomUUID().toString();

    when(taskFileRepository.findById(fileId)).thenReturn(Optional.empty());

    // when
    var exception =
        assertThrows(TaskFileNotFoundException.class, () -> taskFileService.getFileById(fileId));

    // then
    assertEquals("File with id %s not found".formatted(fileId), exception.getMessage());
  }

  @Test
  void testDeleteFile_success() {
    // given
    var fileId = UUID.randomUUID().toString();

    when(taskFileRepository.existsById(fileId)).thenReturn(true);

    // when
    assertDoesNotThrow(() -> taskFileService.deleteFile(fileId));
  }

  @Test
  void testDeleteFile_fileNotFound() {
    // given
    var fileId = UUID.randomUUID().toString();

    when(taskFileRepository.existsById(fileId)).thenReturn(false);

    // when
    var exception =
        assertThrows(TaskFileNotFoundException.class, () -> taskFileService.deleteFile(fileId));

    // then
    assertEquals("File with id %s not found".formatted(fileId), exception.getMessage());
  }
}
