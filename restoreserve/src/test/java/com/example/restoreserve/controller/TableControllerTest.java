package com.example.restoreserve.controller;

import com.example.restoreserve.dto.TableResponseDTO;
import com.example.restoreserve.service.RestaurantTableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TableControllerTest {

  private MockMvc mockMvc;

  @Mock
  private RestaurantTableService tableService;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(new TableController(tableService)).build();
  }

  @Test
  void should_get_all_tables() throws Exception {
    when(tableService.findAll()).thenReturn(List.of(new TableResponseDTO(1L, "Mesa 1", 4)));

    mockMvc.perform(get("/api/v1/tables"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].name").value("Mesa 1"))
        .andExpect(jsonPath("$[0].capacity").value(4));
  }

  @Test
  void should_create_table() throws Exception {
    when(tableService.save(any())).thenReturn(new TableResponseDTO(2L, "Mesa 2", 6));

    mockMvc.perform(post("/api/v1/tables")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"Mesa 2\",\"capacity\":6}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(2))
        .andExpect(jsonPath("$.name").value("Mesa 2"))
        .andExpect(jsonPath("$.capacity").value(6));
  }
}
