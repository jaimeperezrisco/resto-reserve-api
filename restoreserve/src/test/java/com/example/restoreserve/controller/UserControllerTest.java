package com.example.restoreserve.controller;

import com.example.restoreserve.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  private MockMvc mockMvc;

  @Mock
  private UserService userService;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService)).build();
  }

  @Test
  void should_reset_penalization() throws Exception {
    doNothing().when(userService).resetPenalization(10L);

    mockMvc.perform(patch("/api/v1/users/10/reset-penalization"))
        .andExpect(status().isNoContent());
  }
}
