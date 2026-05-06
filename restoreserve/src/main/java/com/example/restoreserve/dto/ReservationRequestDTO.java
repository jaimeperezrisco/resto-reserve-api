package com.example.restoreserve.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReservationRequestDTO(
    @NotNull(message = "restaurant table cant be empty")
    Long id,

    @NotNull(message = "date cant be empty")
    @Future(message = "date not valid") LocalDateTime reservationDate,

    @Min(value = 1, message = "Min 1 person")
    @Max(value = 12, message = "Max 12 persons")
    int numberOfGuests
) {}
