package com.coyoapp.tinytask.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.coyoapp.tinytask.dto.TaskRequest;
import com.coyoapp.tinytask.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIntTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private TaskRepository taskRepository;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void testTaskOperations() throws Exception {
    // 1. Verify no tasks initially
    mockMvc.perform(get("/tasks")).andExpect(status().isOk()).andExpect(jsonPath("$").isEmpty());
    assertTrue(taskRepository.findAll().isEmpty());

    // 2. Create two tasks with multipart file uploads
    createTaskWithFile(
        "Task 1", LocalDateTime.now().plusYears(5), "file1.txt", "text/plain", "simple text");
    createTaskWithFile(
        "Task 2", LocalDateTime.now().plusYears(4), "file2.txt", "text/plain", "example content");

    // 3. Get all tasks and verify
    mockMvc
        .perform(get("/tasks"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("Task 2"))
        .andExpect(jsonPath("$[0].files[0].fileName").value("file2.txt"))
        .andExpect(jsonPath("$[0].files[0].fileType").value("text/plain"))
        .andExpect(jsonPath("$[0].files[0].fileSize").value("example content".getBytes().length))
        .andExpect(jsonPath("$[1].name").value("Task 1"))
        .andExpect(jsonPath("$[1].files[0].fileName").value("file1.txt"))
        .andExpect(jsonPath("$[1].files[0].fileType").value("text/plain"))
        .andExpect(jsonPath("$[1].files[0].fileSize").value("simple text".getBytes().length));

    var tasks = taskRepository.findAll();
    assertEquals(2, tasks.size());

    // 4. Delete the tasks
    mockMvc.perform(delete("/tasks/" + tasks.get(0).getId())).andExpect(status().isNoContent());
    mockMvc.perform(delete("/tasks/" + tasks.get(1).getId())).andExpect(status().isNoContent());

    // 5. Verify no tasks exist after deletion
    mockMvc.perform(get("/tasks")).andExpect(status().isOk()).andExpect(jsonPath("$").isEmpty());
    assertTrue(taskRepository.findAll().isEmpty());
  }

  private void createTaskWithFile(
      String taskName, LocalDateTime dueDate, String fileName, String fileType, String fileContent)
      throws Exception {

    // Create task request payload
    var taskRequest = TaskRequest.builder().name(taskName).dueDate(dueDate).build();
    var taskRequestPayload =
        new MockMultipartFile(
            "taskRequest",
            "",
            "application/json",
            objectMapper.writeValueAsString(taskRequest).getBytes());

    // Create file upload
    var taskFile = new MockMultipartFile("files", fileName, fileType, fileContent.getBytes());

    // Perform multipart request to create task
    mockMvc
        .perform(
            multipart("/tasks")
                .file(taskRequestPayload)
                .file(taskFile)
                .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated());
  }
}
