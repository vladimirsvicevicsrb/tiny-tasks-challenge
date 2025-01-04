package com.coyoapp.tinytask.web;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.coyoapp.tinytask.dto.TaskFileResponse;
import com.coyoapp.tinytask.dto.TaskRequest;
import com.coyoapp.tinytask.dto.TaskResponse;
import com.coyoapp.tinytask.service.TaskFileService;
import com.coyoapp.tinytask.service.TaskService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

class TaskControllerTest extends BaseControllerTest {

  private static final String PATH = "/tasks";

  @MockBean private TaskService taskService;
  @MockBean private TaskFileService taskFileService;

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
                      "dueDate": "2025-01-01T00:00:00",
                      files: [
                        {
                           "id": "%s",
                           "taskId": "%s",
                           "fileName": "file1.txt",
                           "fileType": "text/plain",
                           "fileSize": 1024
                        }
                      ]
                  },
                  {
                      "id": "%s",
                      "name": "Task 2",
                      "dueDate": "2025-02-01T00:00:00",
                      "files": null
                  },
                  {
                      "id": "%s",
                      "name": "Task 3",
                      "dueDate": null,
                      "files":null
                  }
              ]
            """
            .formatted(taskId1, taskId1, taskId1, taskId2, taskId3);

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
            .files(
                Set.of(
                    TaskFileResponse.builder()
                        .id(taskId1.toString())
                        .taskId(taskId1.toString())
                        .fileName("file1.txt")
                        .fileType("text/plain")
                        .fileSize(1024L)
                        .build()))
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
    TaskRequest taskRequest = createTaskRequest("Task 1", LocalDateTime.now().plusDays(5));
    MockMultipartFile taskRequestPayload = createTaskRequestPayload(taskRequest);

    MockMultipartFile taskFile =
        createTaskFile("file1.txt", "text/plain", "simple text".getBytes());

    TaskResponse expectedResponse =
        createTaskResponse(
            taskRequest.getName(),
            taskRequest.getDueDate(),
            taskFile.getOriginalFilename(),
            taskFile.getContentType(),
            (long) taskFile.getBytes().length);

    when(taskService.createTask(taskRequest, List.of(taskFile))).thenReturn(expectedResponse);

    // when
    // then
    mockMvc
        .perform(
            multipart(PATH)
                .file(taskRequestPayload)
                .file(taskFile)
                .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
  }

  @Test
  void shouldThrowBadRequestException_whenInvalidCreateTaskPayload() throws Exception {
    // given
    var taskRequest =
        TaskRequest.builder()
            .name("")
            .dueDate(LocalDateTime.now().minusDays(1)) // past due date
            .build();

    var taskRequestPayload =
        new MockMultipartFile(
            "taskRequest",
            "",
            "application/json",
            objectMapper.writeValueAsString(taskRequest).getBytes());
    var taskFile =
        new MockMultipartFile("files", "file1.txt", "text/plain", "simple text".getBytes());

    // when
    // then
    mockMvc
        .perform(
            multipart(PATH)
                .file(taskRequestPayload)
                .file(taskFile)
                .contentType(MediaType.MULTIPART_FORM_DATA))
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

  private TaskRequest createTaskRequest(String name, LocalDateTime dueDate) {
    return TaskRequest.builder().name(name).dueDate(dueDate).build();
  }

  private MockMultipartFile createTaskRequestPayload(TaskRequest taskRequest) throws Exception {
    return new MockMultipartFile(
        "taskRequest",
        "",
        "application/json",
        objectMapper.writeValueAsString(taskRequest).getBytes());
  }

  private MockMultipartFile createTaskFile(String fileName, String fileType, byte[] content) {
    return new MockMultipartFile("files", fileName, fileType, content);
  }

  private TaskResponse createTaskResponse(
      String name, LocalDateTime dueDate, String fileName, String fileType, Long fileSize) {
    String taskId = UUID.randomUUID().toString();
    return TaskResponse.builder()
        .id(taskId)
        .name(name)
        .dueDate(dueDate)
        .files(
            Set.of(
                TaskFileResponse.builder()
                    .id(UUID.randomUUID().toString())
                    .taskId(taskId)
                    .fileName(fileName)
                    .fileType(fileType)
                    .fileSize(fileSize)
                    .build()))
        .build();
  }
}
