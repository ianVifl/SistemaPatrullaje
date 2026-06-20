package com.ianvifit.sistemapatrullaje.model;

public enum Prioridad {
    BAJA("BAJA"),
    MEDIA("MEDIA"),
    ALTA("ALTA"),
    CRITICA("CRITICA");

    private final String valor;

    Prioridad(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}

