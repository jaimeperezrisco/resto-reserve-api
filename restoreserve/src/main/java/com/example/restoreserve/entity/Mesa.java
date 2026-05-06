package com.example.restoreserve.entity;

import jakarta.persistence.*;

@Entity
public class Mesa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private int capacity;
}
