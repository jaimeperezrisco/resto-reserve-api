package com.example.restoreserve.repository;

import com.example.restoreserve.entity.Reservation;
import com.example.restoreserve.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserUsername(String userUsername);
    List<Reservation> findByRestaurantTableIdAndReservationDateBetween(
        Long tableId,
        LocalDateTime start,
        LocalDateTime end
    );
    List<Reservation> findCompletedReservations();
}
