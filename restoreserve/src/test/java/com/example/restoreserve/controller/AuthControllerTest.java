package com.example.restoreserve.controller;

import com.example.restoreserve.dto.AuthResponseDTO;
import com.example.restoreserve.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

  private MockMvc mockMvc;

  @Mock
  private AuthService authService;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(authService)).build();
  }

  @Test
  void should_login_and_return_token() throws Exception {
    when(authService.login(any())).thenReturn(new AuthResponseDTO("token-123"));

    mockMvc.perform(post("/api/v1/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"user\",\"password\":\"pass\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value("token-123"));
  }

  @Test
  void should_register_and_return_token() throws Exception {
    when(authService.register(any())).thenReturn(new AuthResponseDTO("token-456"));

    mockMvc.perform(post("/api/v1/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"user\",\"password\":\"pass\",\"fullName\":\"User Example\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value("token-456"));
  }
}
