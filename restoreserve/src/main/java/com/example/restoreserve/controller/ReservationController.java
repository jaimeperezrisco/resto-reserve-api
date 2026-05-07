package com.example.restoreserve.controller;

import com.example.restoreserve.dto.ReservationRequestDTO;
import com.example.restoreserve.entity.Reservation;
import com.example.restoreserve.service.ReservationService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

  @Autowired
  private ReservationService reservationService;

  @GetMapping
  public List<Reservation> getAllReservations() {
    return reservationService.findAll();
  }

}
