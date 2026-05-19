package com.example.restoreserve.service;

import com.example.restoreserve.entity.User;
import com.example.restoreserve.entity.UserStatus;
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

  public User findById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
  }

  public User save(User user) {
    return userRepository.save(user);
  }

  public void resetPenalization(Long id) {
    User user = findById(id);
    user.setPenalizationPoints(0);
    user.setStatus(UserStatus.ACTIVE);
    userRepository.save(user);
  }

}

