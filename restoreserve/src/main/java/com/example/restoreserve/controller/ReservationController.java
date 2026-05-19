package com.example.restoreserve.controller;

import com.example.restoreserve.dto.ReservationRequestDTO;
import com.example.restoreserve.dto.ReservationResponseDTO;
import com.example.restoreserve.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

  private final ReservationService reservationService;

  public ReservationController(ReservationService reservationService) {
    this.reservationService = reservationService;
  }

  @PostMapping
  public ResponseEntity<ReservationResponseDTO> create(
      @Valid @RequestBody ReservationRequestDTO dto) {

    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return new ResponseEntity<>(reservationService.createReservation(dto, username), HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<ReservationResponseDTO>> getAll() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return ResponseEntity.ok(reservationService.getReservations(username));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> cancel(@PathVariable Long id) {
    reservationService.cancelReservation(id);
    return ResponseEntity.noContent().build();
  }
}
