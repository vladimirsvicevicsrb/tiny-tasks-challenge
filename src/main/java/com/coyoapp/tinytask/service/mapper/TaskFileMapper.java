package com.coyoapp.tinytask.service.mapper;

import com.coyoapp.tinytask.domain.TaskFile;
import com.coyoapp.tinytask.dto.TaskFileResponse;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class TaskFileMapper {

  public TaskFileResponse toResponse(TaskFile taskFile) {
    return TaskFileResponse.builder()
        .id(taskFile.getId())
        .taskId(taskFile.getTask().getId())
        .fileName(taskFile.getFileName())
        .fileType(taskFile.getFileType())
        .fileSize(taskFile.getFileSize())
        .build();
  }

  public Set<TaskFileResponse> toResponse(Set<TaskFile> taskFiles) {
    return taskFiles.stream().map(this::toResponse).collect(Collectors.toSet());
  }
}
