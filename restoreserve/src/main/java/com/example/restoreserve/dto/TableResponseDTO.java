package com.example.restoreserve.dto;

public record TableResponseDTO(
    Long id,
    String name,
    int capacity,
    boolean vip) {}
