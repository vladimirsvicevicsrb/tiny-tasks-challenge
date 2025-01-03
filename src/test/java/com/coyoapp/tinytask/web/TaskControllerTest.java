package com.coyoapp.tinytask.web;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.coyoapp.tinytask.dto.TaskRequest;
import com.coyoapp.tinytask.dto.TaskResponse;
import com.coyoapp.tinytask.service.TaskService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

class TaskControllerTest extends BaseControllerTest {

  private static final String PATH = "/tasks";

  @MockBean private TaskService taskService;

  @Test
  void shouldReturnTasks() throws Exception {
    // given
    var taskId1 = UUID.randomUUID();
    var taskId2 = UUID.randomUUID();
    var taskId3 = UUID.randomUUID();

    String expectedTasksJson =
        """
              [
                  {
                      "id": "%s",
                      "name": "Task 1",
                      "dueDate": "2025-01-01T00:00:00"
                  },
                  {
                      "id": "%s",
                      "name": "Task 2",
                      "dueDate": "2025-02-01T00:00:00"
                  },
                  {
                      "id": "%s",
                      "name": "Task 3",
                      "dueDate": null
                  }
              ]
            """
            .formatted(taskId1, taskId2, taskId3);

    when(taskService.getTasks()).thenReturn(getTasksData(taskId1, taskId2, taskId3));

    // when
    // then
    mockMvc
        .perform(get(PATH).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(expectedTasksJson, true));
  }

  @Test
  void shouldReturnEmptyTasks_whenNoTasks() throws Exception {
    // given

    when(taskService.getTasks()).thenReturn(List.of());

    // when
    // then
    mockMvc
        .perform(get(PATH).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));
  }

  private List<TaskResponse> getTasksData(UUID taskId1, UUID taskId2, UUID taskId3) {
    var task1 =
        TaskResponse.builder()
            .id(taskId1.toString())
            .name("Task 1")
            .dueDate(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
            .build();

    var task2 =
        TaskResponse.builder()
            .id(taskId2.toString())
            .name("Task 2")
            .dueDate(LocalDateTime.of(2025, 2, 1, 0, 0, 0))
            .build();

    var task3 = TaskResponse.builder().id(taskId3.toString()).name("Task 3").build();

    return List.of(task1, task2, task3);
  }

  @Test
  void shouldCreateTask() throws Exception {
    // given
    var givenTaskName = "Task 1";
    var givenDueDate = LocalDateTime.now().plusDays(5); // future due date

    var taskRequest = TaskRequest.builder().name(givenTaskName).dueDate(givenDueDate).build();

    var taskResponse =
        TaskResponse.builder()
            .id(UUID.randomUUID().toString())
            .name(givenTaskName)
            .dueDate(givenDueDate)
            .build();

    when(taskService.createTask(taskRequest, null)).thenReturn(taskResponse);

    // when
    // then
    mockMvc
        .perform(
            post(PATH)
                .content(objectMapper.writeValueAsString(taskRequest))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(content().json(objectMapper.writeValueAsString(taskResponse)));
  }

  @Test
  void shouldThrowBadRequestException_whenInvalidCreateTaskPayload() throws Exception {
    // given
    var taskRequest =
        TaskRequest.builder()
            .name("")
            .dueDate(LocalDateTime.now().minusDays(1)) // past due date
            .build();

    // when
    // then
    mockMvc
        .perform(
            post(PATH)
                .content(objectMapper.writeValueAsString(taskRequest))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(
            content()
                .json(
                    """
        {
            "message": "Validation failed",
            "details": "One or more fields are invalid",
            "fieldErrors": {
                "dueDate": "must be a future date",
                "name": "must not be empty"
            }
        }
        """,
                    false));
  }

  @Test
  void shouldDeleteTask() throws Exception {
    // given
    var taskId = UUID.randomUUID().toString();

    doNothing().when(taskService).deleteTask(taskId);

    // when
    // then
    mockMvc.perform(delete(PATH + "/{taskId}", taskId)).andExpect(status().isNoContent());
  }
}
