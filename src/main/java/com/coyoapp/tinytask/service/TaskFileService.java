package com.coyoapp.tinytask.service;

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
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskFileService {

  private final TaskRepository taskRepository;
  private final TaskFileRepository taskFileRepository;
  private final TaskFileMapper taskFileMapper;

  public Task uploadFiles(Task task, List<MultipartFile> files) {
    if (CollectionUtils.isEmpty(files)) {
      return task;
    }

    try {
      for (MultipartFile file : files) {
        TaskFile taskFile = new TaskFile();
        taskFile.setTask(task);
        taskFile.setFileName(file.getOriginalFilename());
        taskFile.setFileType(file.getContentType());
        taskFile.setFileSize(file.getSize());
        taskFile.setFileContent(file.getBytes());

        task.getFiles().add(taskFile);
      }

    } catch (IOException e) {
      throw new TaskFileUploadFailedException(
          "File upload for task %s failed".formatted(task.getId()), e);
    }

    return taskRepository.save(task);
  }

  public Set<TaskFileResponse> getFilesByTaskId(String taskId) {
    final Task task =
        taskRepository
            .findById(taskId)
            .orElseThrow(
                () -> new TaskNotFoundException("Task not found with id %s".formatted(taskId)));
    return taskFileMapper.toResponse(task.getFiles());
  }

  public TaskFile getFileById(String fileId) {
    return taskFileRepository
        .findById(fileId)
        .orElseThrow(
            () -> new TaskFileNotFoundException("File with id %s not found".formatted(fileId)));
  }
}
