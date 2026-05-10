package com.example.restoreserve.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDTO(
    @NotBlank(message = "El usuario es obligatorio")
    String username,

    @NotBlank(message = "La contraseña es obligatoria")
    String password,

    @NotBlank(message = "El nombre completo es obligatorio")
    String fullName
) {}
