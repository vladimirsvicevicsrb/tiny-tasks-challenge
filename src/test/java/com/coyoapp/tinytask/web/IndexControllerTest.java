package com.coyoapp.tinytask.web;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.coyoapp.tinytask.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

class IndexControllerTest extends BaseControllerTest {

  private static final String PATH = "/";

  @MockBean private TaskService taskService;

  @Test
  void shouldReturnIndexContent() throws Exception {
    // when
    // then
    mockMvc
        .perform(get(PATH))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Tiny Task Server is up and running.")));
  }
}
