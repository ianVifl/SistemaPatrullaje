package com.ianvifit.sistemapatrullaje.model;

public enum TipoIncidente {
    ROBO("ROBO"),
    ASALTO("ASALTO"),
    VANDALISMO("VANDALISMO"),
    ACCIDENTE("ACCIDENTE"),
    DISTURBIO("DISTURBIO"),
    OTRO("OTRO");

    private final String valor;

    TipoIncidente(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}

