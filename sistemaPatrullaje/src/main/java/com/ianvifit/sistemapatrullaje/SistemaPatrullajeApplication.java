package com.ianvifit.sistemapatrullaje;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SistemaPatrullajeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaPatrullajeApplication.class, args);
    }

}
