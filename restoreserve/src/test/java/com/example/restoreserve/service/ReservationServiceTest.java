package com.example.restoreserve.service;

import com.example.restoreserve.dto.ReservationRequestDTO;
import com.example.restoreserve.dto.ReservationResponseDTO;
import com.example.restoreserve.entity.Reservation;
import com.example.restoreserve.entity.ReservationStatus;
import com.example.restoreserve.entity.RestaurantTable;
import com.example.restoreserve.entity.Role;
import com.example.restoreserve.entity.User;
import com.example.restoreserve.entity.UserStatus;
import com.example.restoreserve.exception.UserBannedException;
import com.example.restoreserve.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

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

  @Test
  void should_create_reservation_when_data_valid() {
    LocalDateTime reservationDate = LocalDateTime.now().plusHours(3);
    ReservationRequestDTO dto = new ReservationRequestDTO(1L, reservationDate, 2);

    User user = new User();
    ReflectionTestUtils.setField(user, "username", "user");
    ReflectionTestUtils.setField(user, "fullName", "User Example");
    ReflectionTestUtils.setField(user, "role", Role.USER);
    ReflectionTestUtils.setField(user, "status", UserStatus.ACTIVE);

    RestaurantTable table = new RestaurantTable();
    ReflectionTestUtils.setField(table, "id", 1L);
    ReflectionTestUtils.setField(table, "name", "Mesa 1");
    ReflectionTestUtils.setField(table, "capacity", 4);

    when(userService.findByUsername("user")).thenReturn(user);
    when(tableService.findById(1L)).thenReturn(table);
    when(reservationRepository.findByRestaurantTableIdAndReservationDateBetween(eq(1L), any(), any()))
        .thenReturn(List.of());
    when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> {
      Reservation reservation = invocation.getArgument(0);
      ReflectionTestUtils.setField(reservation, "id", 10L);
      return reservation;
    });

    ReservationResponseDTO response = reservationService.createReservation(dto, "user");

    assertEquals(10L, response.id());
    assertEquals("Mesa 1", response.tableName());
    assertEquals("User Example", response.customerName());
    assertEquals(ReservationStatus.ACTIVE, response.status());
    verify(reservationRepository).save(any(Reservation.class));
  }

  @Test
  void should_fail_when_user_is_banned() {
    LocalDateTime reservationDate = LocalDateTime.now().plusHours(3);
    ReservationRequestDTO dto = new ReservationRequestDTO(1L, reservationDate, 2);

    User user = new User();
    ReflectionTestUtils.setField(user, "username", "user");
    ReflectionTestUtils.setField(user, "fullName", "User Example");
    ReflectionTestUtils.setField(user, "role", Role.USER);
    ReflectionTestUtils.setField(user, "status", UserStatus.BANNED);

    when(userService.findByUsername("user")).thenReturn(user);

    assertThrows(UserBannedException.class, () -> reservationService.createReservation(dto, "user"));
    verify(reservationRepository, never()).save(any());
  }

  @Test
  void should_apply_penalty_on_late_cancellation() {
    User user = new User();
    ReflectionTestUtils.setField(user, "penalizationPoints", 6);
    ReflectionTestUtils.setField(user, "status", UserStatus.ACTIVE);

    Reservation reservation = new Reservation();
    ReflectionTestUtils.setField(reservation, "id", 10L);
    ReflectionTestUtils.setField(reservation, "reservationDate", LocalDateTime.now().plusMinutes(30));
    ReflectionTestUtils.setField(reservation, "status", ReservationStatus.ACTIVE);
    ReflectionTestUtils.setField(reservation, "user", user);

    when(reservationRepository.findById(10L)).thenReturn(java.util.Optional.of(reservation));
    when(userService.save(any(User.class))).thenReturn(user);
    when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

    reservationService.cancelReservation(10L);

    assertEquals(8, (int) ReflectionTestUtils.getField(user, "penalizationPoints"));
    assertEquals(UserStatus.BANNED, ReflectionTestUtils.getField(user, "status"));
  }
}
