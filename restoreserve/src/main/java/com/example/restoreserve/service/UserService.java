package com.example.restoreserve.service;

import com.example.restoreserve.entity.User;
import com.example.restoreserve.exception.ResourceNotFoundException;
import com.example.restoreserve.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User findByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
  }

}

