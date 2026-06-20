package com.ianvifit.sistemapatrullaje.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ianvifit.sistemapatrullaje.model.Incidente;
import com.ianvifit.sistemapatrullaje.model.Prioridad;
import com.ianvifit.sistemapatrullaje.model.TipoIncidente;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Random random = new Random();

    private final List<String> sectores = Arrays.asList("Centro Cuernavaca", "Chapultepec", "Lomas", "Jiutepec", "Tepoztlán");
    private BufferedReader csvReader;

    // Quitamos ObjectMapper de este constructor
    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostConstruct
    public void init() {
        System.out.println("Buscando el archivo datos.csv en resources...");
        try {
            csvReader = new BufferedReader(new InputStreamReader(new ClassPathResource("datos.csv").getInputStream()));
            csvReader.readLine(); // Saltamos la primera línea
            System.out.println("¡Archivo CSV conectado con éxito!");
        } catch (Exception e) {
            System.err.println("ERROR FATAL: No se encontró el archivo datos.csv. Revisa que esté en src/main/resources");
        }
    }

    @Scheduled(fixedRate = 2000)
    public void procesarFilaCsv() {
        if (csvReader == null) return;

        try {
            String linea = csvReader.readLine();

            if (linea == null) {
                System.out.println("Fin del CSV alcanzado. Reiniciando la lectura...");
                init();
                return;
            }

            String[] columnas = linea.split(",");

            if (columnas.length > 4) {
                String tipoDelitoCsv = columnas[4].replace("\"", "").trim();

                Incidente incidente = new Incidente();
                incidente.setTipoIncidente(TipoIncidente.OTRO);
                incidente.setPrioridad(Prioridad.ALTA);
                incidente.setSector(sectores.get(random.nextInt(sectores.size())));

                incidente.setDescripcion("Histórico INEGI: " + tipoDelitoCsv);

                String json = objectMapper.writeValueAsString(incidente);
                kafkaTemplate.send("alertas-seguridad", json);

                System.out.println("🚨 Kafka procesó: " + tipoDelitoCsv + " en " + incidente.getSector());
            }

        } catch (Exception e) {
            System.err.println("❌ Error leyendo línea del CSV: " + e.getMessage());
        }
    }
}