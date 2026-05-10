package com.example.restoreserve.controller;

import com.example.restoreserve.dto.AuthRequestDTO;
import com.example.restoreserve.dto.AuthResponseDTO;
import com.example.restoreserve.dto.RegisterRequestDTO;
import com.example.restoreserve.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO dto) {
    return ResponseEntity.ok(authService.login(dto));
  }

  @PostMapping("/register")
  public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto) {
    return ResponseEntity.ok(authService.register(dto));
  }
}
