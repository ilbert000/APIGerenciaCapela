package com.calendario.api.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "events")
@Data // Gera automaticamente getters, setters, equals, hashCode e toString via Lombok
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
}
