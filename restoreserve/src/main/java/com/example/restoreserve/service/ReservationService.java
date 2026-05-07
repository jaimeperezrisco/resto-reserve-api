package com.example.restoreserve.service;

import com.example.restoreserve.dto.ReservationRequestDTO;
import com.example.restoreserve.entity.Reservation;
import com.example.restoreserve.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

  private final ReservationRepository reservationRepository;

  public ReservationService(ReservationRepository reservationRepository) {
    this.reservationRepository = reservationRepository;
  }

  public List<Reservation> findAll() {
    return reservationRepository.findAll();
  }

}
