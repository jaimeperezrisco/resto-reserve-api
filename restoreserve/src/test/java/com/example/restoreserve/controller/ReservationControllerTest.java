package com.example.restoreserve.controller;

import com.example.restoreserve.dto.ReservationResponseDTO;
import com.example.restoreserve.entity.ReservationStatus;
import com.example.restoreserve.service.ReservationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {

  private MockMvc mockMvc;

  @Mock
  private ReservationService reservationService;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(new ReservationController(reservationService)).build();
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken("user", "pass", List.of())
    );
  }

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void should_create_reservation() throws Exception {
    LocalDateTime reservationDate = LocalDateTime.now().plusDays(1);
    ReservationResponseDTO response = new ReservationResponseDTO(
        10L,
        "Mesa 1",
        "User Example",
        reservationDate,
        2,
        ReservationStatus.ACTIVE
    );
    when(reservationService.createReservation(any(), anyString())).thenReturn(response);

    mockMvc.perform(post("/api/v1/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"tableId\":1,\"reservationDate\":\"2026-12-31T20:00:00\",\"numberOfGuests\":2}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(10))
        .andExpect(jsonPath("$.tableName").value("Mesa 1"))
        .andExpect(jsonPath("$.customerName").value("User Example"))
        .andExpect(jsonPath("$.status").value("ACTIVE"));
  }

  @Test
  void should_get_all_reservations() throws Exception {
    ReservationResponseDTO response = new ReservationResponseDTO(
        11L,
        "Mesa 2",
        "User Example",
        LocalDateTime.now().plusDays(2),
        4,
        ReservationStatus.ACTIVE
    );
    when(reservationService.getReservations(anyString())).thenReturn(List.of(response));

    mockMvc.perform(get("/api/v1/reservations"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(11))
        .andExpect(jsonPath("$[0].tableName").value("Mesa 2"))
        .andExpect(jsonPath("$[0].status").value("ACTIVE"));
  }

  @Test
  void should_cancel_reservation() throws Exception {
    doNothing().when(reservationService).cancelReservation(10L);

    mockMvc.perform(delete("/api/v1/reservations/10"))
        .andExpect(status().isNoContent());
  }
}
