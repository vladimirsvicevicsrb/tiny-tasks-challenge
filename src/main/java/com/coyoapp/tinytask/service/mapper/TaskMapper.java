package com.coyoapp.tinytask.service.mapper;

import com.coyoapp.tinytask.domain.Task;
import com.coyoapp.tinytask.domain.TaskFile;
import com.coyoapp.tinytask.dto.TaskRequest;
import com.coyoapp.tinytask.dto.TaskResponse;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskMapper {

  private final TaskFileMapper taskFileMapper;

  public Task toTask(TaskRequest request) {
    Task task = new Task();
    task.setName(request.getName());
    task.setDueDate(request.getDueDate());
    return task;
  }

  public TaskResponse toResponse(Task task) {
    return TaskResponse.builder()
        .id(task.getId())
        .name(task.getName())
        .dueDate(task.getDueDate())
        .files(taskFileMapper.toResponse(task.getFiles()))
        .build();
  }
}
