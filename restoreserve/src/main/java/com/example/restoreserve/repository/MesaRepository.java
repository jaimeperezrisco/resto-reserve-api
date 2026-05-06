package com.example.restoreserve.repository;

import com.example.restoreserve.entity.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MesaRepository extends JpaRepository<Mesa,Long> {
    Optional<Mesa> findByName(String name);
}
