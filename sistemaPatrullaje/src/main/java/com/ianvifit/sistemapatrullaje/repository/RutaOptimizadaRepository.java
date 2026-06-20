package com.ianvifit.sistemapatrullaje.repository;

import com.ianvifit.sistemapatrullaje.model.RutaOptimizada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RutaOptimizadaRepository extends JpaRepository<RutaOptimizada, Long> {
}

