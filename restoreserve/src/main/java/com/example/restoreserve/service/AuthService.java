package com.example.restoreserve.service;

import com.example.restoreserve.dto.AuthRequestDTO;
import com.example.restoreserve.dto.AuthResponseDTO;
import com.example.restoreserve.dto.RegisterRequestDTO;
import com.example.restoreserve.entity.Role;
import com.example.restoreserve.entity.User;
import com.example.restoreserve.repository.UserRepository;
import com.example.restoreserve.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public AuthService(
      AuthenticationManager authenticationManager,
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      JwtService jwtService
  ) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  public AuthResponseDTO login(AuthRequestDTO dto) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(dto.username(), dto.password())
    );

    User user = userRepository.findByUsername(dto.username())
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    String token = jwtService.generateToken(user.getUsername());
    return new AuthResponseDTO(token);
  }

  public AuthResponseDTO register(RegisterRequestDTO dto) {
    if (userRepository.findByUsername(dto.username()).isPresent()) {
      throw new RuntimeException("El usuario ya existe");
    }

    User user = new User();
    user.setUsername(dto.username());
    user.setPassword(passwordEncoder.encode(dto.password()));
    user.setFullName(dto.fullName());
    user.setRole(Role.ROLE_USER);

    userRepository.save(user);

    String token = jwtService.generateToken(user.getUsername());
    return new AuthResponseDTO(token);
  }
}
