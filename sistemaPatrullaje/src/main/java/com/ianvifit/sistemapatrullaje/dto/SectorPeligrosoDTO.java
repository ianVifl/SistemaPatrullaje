package com.ianvifit.sistemapatrullaje.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SectorPeligrosoDTO {
    private String sector;
    private Long totalDelitos;
}
