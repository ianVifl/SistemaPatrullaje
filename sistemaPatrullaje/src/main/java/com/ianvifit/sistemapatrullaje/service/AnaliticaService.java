package com.ianvifit.sistemapatrullaje.service;

import com.ianvifit.sistemapatrullaje.dto.SectorPeligrosoDTO;
import com.ianvifit.sistemapatrullaje.repository.IncidenteRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnaliticaService {
    private final IncidenteRepository incidenteRepository;

    public AnaliticaService(IncidenteRepository incidenteRepository) {
        this.incidenteRepository = incidenteRepository;
    }

    public List<SectorPeligrosoDTO> obtenerTopSectoresPeligrosos() {
        List<Object[]> resultados = incidenteRepository.findTop3SectoresPeligrosos();
        List<SectorPeligrosoDTO> topSectores = new ArrayList<>();

        for (Object[] fila : resultados) {
            String sector = (String) fila[0];
            Long total = ((Number) fila[1]).longValue();
            topSectores.add(new SectorPeligrosoDTO(sector, total));
        }

        return topSectores;
    }
}
