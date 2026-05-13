package com.example.restoreserve.dto;

import com.example.restoreserve.entity.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationResponseDTO(
    Long id,
    String tableName,
    String customerName,
    LocalDateTime reservationDate,
    int numberOfGuests,
    ReservationStatus status
) {}
