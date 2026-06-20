package com.ianvifit.sistemapatrullaje.repository;

import com.ianvifit.sistemapatrullaje.model.Incidente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IncidenteRepository extends JpaRepository<Incidente, Long> {
    @Query("SELECT i.sector, COUNT(i) as total FROM Incidente i GROUP BY i.sector ORDER BY total DESC LIMIT 3")
    List<Object[]> findTop3SectoresPeligrosos();
}
