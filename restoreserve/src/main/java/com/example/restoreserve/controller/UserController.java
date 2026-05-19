package com.example.restoreserve.controller;

import com.example.restoreserve.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PatchMapping("/{id}/reset-penalization")
  public ResponseEntity<Void> resetPenalization(@PathVariable Long id) {
    userService.resetPenalization(id);
    return ResponseEntity.noContent().build();
  }
}
