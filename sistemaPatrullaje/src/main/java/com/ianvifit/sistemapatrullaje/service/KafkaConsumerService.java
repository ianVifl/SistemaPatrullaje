package com.ianvifit.sistemapatrullaje.service;


import com.google.gson.Gson;
import com.ianvifit.sistemapatrullaje.model.Incidente;
import com.ianvifit.sistemapatrullaje.repository.IncidenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class KafkaConsumerService {

    private final IncidenteRepository incidenteRepository;
    private final ObjectMapper objectMapper;

    public KafkaConsumerService(IncidenteRepository incidenteRepository, ObjectMapper objectMapper) {
        this.incidenteRepository = incidenteRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "alertas-seguridad", groupId = "patrullaje-group")
    public void consumirIncidente(String mensajeJson) {
        try {
            Incidente incidente = objectMapper.readValue(mensajeJson, Incidente.class);

            incidenteRepository.save(incidente);

            System.out.println("Consumidor atrapó y guardó: " + incidente.getTipoIncidente() + " en " + incidente.getSector());

        } catch (Exception e) {
            System.err.println("ERROR FATAL en Consumidor: " + e.getMessage());
        }
    }


}
