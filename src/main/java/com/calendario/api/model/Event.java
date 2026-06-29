package com.calendario.api.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "events")
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String color;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false)
    private String status;

    @Column
    private String contato;

    @Column
    private String tipoEvento;

    @Column
    private String urgencia;

    @Column
    private Integer quantidade;

    @Column
    private String horarioInicio;

    @Column
    private String horarioFim;

    @Column(columnDefinition = "TEXT")
    private String observacoes;
}
