package com.calendario.api.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EventRequest {
    private String title;
    private String name;
    private String email;
    private String contato;
    private LocalDate date;
    private String horario;
    private String motivo;
    private String type;
    private String color;
    private String description;
    private String codigo;
    private String status;
    private String tipoEvento;
    private String urgencia;
    private Integer quantidade;
    private String horarioInicio;
    private String horarioFim;
    private String observacoes;
}
