package com.example.restoreserve.service;

import com.example.restoreserve.dto.ReservationRequestDTO;
import com.example.restoreserve.dto.ReservationResponseDTO;
import com.example.restoreserve.entity.*;
import com.example.restoreserve.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

  private final ReservationRepository reservationRepository;
  private final UserService userService;
  private final RestaurantTableService tableService;

  public ReservationResponseDTO createReservation(ReservationRequestDTO dto, String username) {
    //Obtener entidades
    User user = userService.findByUsername(username);
    RestaurantTable table = tableService.findById(dto.tableId());

    //Validar margen de 2 horas
    LocalDateTime start = dto.reservationDate().minusHours(2);
    LocalDateTime end = dto.reservationDate().plusHours(2);

    List<Reservation> overlapping = reservationRepository
        .findByRestaurantTableIdAndReservationDateBetween(table.getId(), start, end);

    //Si hay alguna reserva que no esté CANCELLED en ese horario, error
    boolean hasConflict = overlapping.stream()
        .anyMatch(r -> r.getStatus() != ReservationStatus.CANCELLED);

    if (hasConflict) {
      throw new RuntimeException("La mesa ya tiene una reserva en ese rango de 2 horas.");
    }

    //Mapear DTO a Entidad
    Reservation reservation = new Reservation();
    reservation.setReservationDate(dto.reservationDate());
    reservation.setNumberOfGuests(dto.numberOfGuests());
    reservation.setStatus(ReservationStatus.ACTIVE);
    reservation.setUser(user);
    reservation.setRestaurantTable(table);

    //guardar y devolver DTO
    return mapToDTO(reservationRepository.save(reservation));
  }

  public List<ReservationResponseDTO> getReservations(String username) {
    User user = userService.findByUsername(username);
    List<Reservation> entities;

    // logica de roles: ADMIN ve todas, USER solo las suyas
    if (user.getRole() == Role.ADMIN) {
      entities = reservationRepository.findAll();
    } else {
      entities = reservationRepository.findByUserUsername(username);
    }

    return entities.stream().map(this::mapToDTO).toList();
  }

  public void cancelReservation(Long id) {
    Reservation res = reservationRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

    // Aplicar Soft Delete
    res.setStatus(ReservationStatus.CANCELLED);
    reservationRepository.save(res);
  }

  //Método auxiliar de mapeo para cumplir con el uso de DTOs
  private ReservationResponseDTO mapToDTO(Reservation res) {
    return new ReservationResponseDTO(
        res.getId(),
        res.getRestaurantTable().getName(),
        res.getUser().getFullName(),
        res.getReservationDate(),
        res.getNumberOfGuests(),
        res.getStatus()
    );
  }
}
