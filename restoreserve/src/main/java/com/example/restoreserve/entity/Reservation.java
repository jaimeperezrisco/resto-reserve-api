package com.example.restoreserve.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

public class Reservation {
    @Id @GeneratedValue
    Long id;

    @ManyToOne(optional = false)
    private Mesa mesa;

    @ManyToOne (optional = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime reservationDate;

    @Column(nullable = false)
    private int numberOfGuests;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
}
