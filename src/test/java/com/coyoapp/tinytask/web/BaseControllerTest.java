package com.coyoapp.tinytask.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
public abstract class BaseControllerTest {

  @Autowired protected ObjectMapper objectMapper;

  @Autowired protected MockMvc mockMvc;
}
