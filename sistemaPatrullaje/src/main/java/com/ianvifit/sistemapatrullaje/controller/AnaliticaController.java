package com.ianvifit.sistemapatrullaje.controller;

import com.ianvifit.sistemapatrullaje.dto.SectorPeligrosoDTO;
import com.ianvifit.sistemapatrullaje.service.AnaliticaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/analitica")
public class AnaliticaController {
    private final AnaliticaService analiticaService;

    public AnaliticaController(AnaliticaService analiticaService) {
        this.analiticaService = analiticaService;
    }

    @GetMapping("/top-sectores")
    public ResponseEntity<List<SectorPeligrosoDTO>> obtenerTopSectores() {
        List<SectorPeligrosoDTO> datos = analiticaService.obtenerTopSectoresPeligrosos();
        return ResponseEntity.ok(datos);
    }
}
