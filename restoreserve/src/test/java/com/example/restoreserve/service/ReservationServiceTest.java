package com.example.restoreserve.service;

import com.example.restoreserve.dto.ReservationRequestDTO;
import com.example.restoreserve.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

  @Mock
  private ReservationRepository reservationRepository;

  @Mock
  private UserService userService;

  @Mock
  private RestaurantTableService tableService;

  @InjectMocks
  private ReservationService reservationService;

  @Test
  void should_not_create_reservation_when_date_its_past() {
    ReservationRequestDTO dto = new ReservationRequestDTO(
        1L,
        LocalDateTime.now().minusHours(1),
        2
    );

    assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(dto, "user"));

    verify(reservationRepository, never()).save(any());
  }
}
