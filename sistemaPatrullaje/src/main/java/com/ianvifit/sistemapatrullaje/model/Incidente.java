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
@Table(name ="incidentes")

public class Incidente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String descripcion;

    @Column(name = "tipo_incidente")
    @Enumerated(EnumType.STRING)
    private TipoIncidente tipoIncidente;

    @Column(name = "prioridad", nullable = false)
    @Enumerated(EnumType.STRING)
    private Prioridad prioridad;

    private String sector;

    @Column(name ="fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
    }


}
