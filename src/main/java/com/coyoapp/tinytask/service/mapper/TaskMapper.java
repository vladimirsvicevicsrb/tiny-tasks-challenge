package com.coyoapp.tinytask.service.mapper;

import com.coyoapp.tinytask.domain.Task;
import com.coyoapp.tinytask.dto.TaskRequest;
import com.coyoapp.tinytask.dto.TaskResponse;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

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
        .build();
  }
}
