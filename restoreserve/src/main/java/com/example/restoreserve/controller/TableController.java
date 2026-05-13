package com.example.restoreserve.controller;

import com.example.restoreserve.dto.TableRequestDTO;
import com.example.restoreserve.dto.TableResponseDTO;
import com.example.restoreserve.service.RestaurantTableService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tables")
public class TableController {
  private final RestaurantTableService tableService;

  public TableController(RestaurantTableService tableService) {
    this.tableService = tableService;
  }

  @GetMapping
  public ResponseEntity<List<TableResponseDTO>> getAll() {
    return ResponseEntity.ok(tableService.findAll());
  }

  @PostMapping
  public ResponseEntity<TableResponseDTO> create(@RequestBody TableRequestDTO dto) {
    return new ResponseEntity<>(tableService.save(dto), HttpStatus.CREATED);
  }
}
