package com.coyoapp.tinytask.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.coyoapp.tinytask.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIntTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private TaskRepository taskRepository;

  @Test
  void testTaskOperations() throws Exception {
    // 1. Verify no tasks initially
    mockMvc.perform(get("/tasks")).andExpect(status().isOk()).andExpect(jsonPath("$").isEmpty());
    assertTrue(taskRepository.findAll().isEmpty());

    // 2. Create two tasks
    mockMvc
        .perform(
            post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "name": "Task 1",
                        "dueDate": "2032-03-31T23:59:59"
                    }
                    """))
        .andExpect(status().isCreated());

    mockMvc
        .perform(
            post("/tasks")
                .contentType("application/json")
                .content(
                    """
                    {
                        "name": "Task 2",
                        "dueDate": "2031-03-31T23:59:59"
                    }
                    """))
        .andExpect(status().isCreated());

    // 3. Get all tasks and verify
    mockMvc
        .perform(get("/tasks"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("Task 2"))
        .andExpect(jsonPath("$[1].name").value("Task 1"));

    var tasks = taskRepository.findAll();
    assertEquals(2, tasks.size());

    // 4. Delete the tasks
    mockMvc.perform(delete("/tasks/" + tasks.get(0).getId())).andExpect(status().isNoContent());
    mockMvc.perform(delete("/tasks/" + tasks.get(1).getId())).andExpect(status().isNoContent());

    // 5. Verify no tasks exist after deletion
    mockMvc.perform(get("/tasks")).andExpect(status().isOk()).andExpect(jsonPath("$").isEmpty());
    assertTrue(taskRepository.findAll().isEmpty());
  }
}
