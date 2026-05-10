package com.example.restoreserve.service;

import com.example.restoreserve.dto.TableRequestDTO;
import com.example.restoreserve.dto.TableResponseDTO;
import com.example.restoreserve.entity.RestaurantTable;
import com.example.restoreserve.repository.RestaurantTableRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantTableService {
  private final RestaurantTableRepository tableRepository;

  public RestaurantTableService(RestaurantTableRepository tableRepository) {
    this.tableRepository = tableRepository;
  }

  public List<TableResponseDTO> findAll() {
    return tableRepository.findAll().stream()
        .map(this::toDTO)
        .toList();
  }

  public TableResponseDTO save(TableRequestDTO dto) {
    RestaurantTable table = new RestaurantTable();
    table.setName(dto.name());
    table.setCapacity(dto.capacity());

    return toDTO(tableRepository.save(table));
  }

  public RestaurantTable findById(Long id) {
    return tableRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));
  }

  private TableResponseDTO toDTO(RestaurantTable table) {
    return new TableResponseDTO(
        table.getId(),
        table.getName(),
        table.getCapacity()
    );
  }
}
