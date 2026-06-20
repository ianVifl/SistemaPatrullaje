package com.ianvifit.sistemapatrullaje.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name= "rutas_optimizadas")
public class RutaOptimizada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String rutaGenerada;

    private double scoreFitnes;

    private LocalDateTime fechaActualizada;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizada = LocalDateTime.now();
    }



}
