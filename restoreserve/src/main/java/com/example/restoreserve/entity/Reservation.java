package com.example.restoreserve.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservations")

public class Reservation {
    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

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
