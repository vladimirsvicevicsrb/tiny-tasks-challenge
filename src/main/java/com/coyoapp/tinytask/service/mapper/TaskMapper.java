package com.coyoapp.tinytask.service.mapper;

import org.springframework.stereotype.Component;

import com.coyoapp.tinytask.domain.Task;
import com.coyoapp.tinytask.dto.TaskRequest;
import com.coyoapp.tinytask.dto.TaskResponse;

@Component
public class TaskMapper {
    
    public Task toTask(TaskRequest request) {
        Task task = new Task();
        task.setName(request.getName());
        return task;
    }

    public TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
            .id(task.getId())
            .name(task.getName())
            .build();
    }

}
