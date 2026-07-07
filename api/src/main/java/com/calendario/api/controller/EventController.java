package com.calendario.api.controller;

import com.calendario.api.model.Event;
import com.calendario.api.model.EventRequest;
import com.calendario.api.repository.EventRepository;
import com.calendario.api.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventos")
@CrossOrigin(origins = "*") // Permite integração com qualquer origem frontend
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EmailService emailService;

    // READ ALL (Listar todos)
    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // CREATE (Inserir novo)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Event createEvent(@RequestBody EventRequest eventRequest) {
        Event event = new Event();
        event.setTitle(getEventTitle(eventRequest));
        event.setDate(eventRequest.getDate());
        event.setColor(eventRequest.getColor() != null ? eventRequest.getColor() : "#ef4444");
        event.setDescription(getEventDescription(eventRequest));
        event.setStatus(getEventStatus(eventRequest));

        Event savedEvent = eventRepository.save(event);

        if (eventRequest.getName() != null && eventRequest.getEmail() != null
                && eventRequest.getHorario() != null && eventRequest.getMotivo() != null) {
            try {
                emailService.sendAdminNotification(
                        eventRequest.getName(),
                        eventRequest.getEmail(),
                        eventRequest.getDate(),
                        eventRequest.getHorario(),
                        eventRequest.getMotivo());
            } catch (Exception ex) {
                System.err.println("Falha ao enviar e-mail de notificação: " + ex.getMessage());
            }
        }

        return savedEvent;
    }

    private String getEventStatus(EventRequest eventRequest) {
        if (eventRequest.getStatus() != null && !eventRequest.getStatus().isBlank()) {
            return eventRequest.getStatus();
        }
        return "Aguardando Atendimento";
    }

    private String getEventTitle(EventRequest eventRequest) {
        if (eventRequest.getTitle() != null && !eventRequest.getTitle().isBlank()) {
            return eventRequest.getTitle();
        }
        return eventRequest.getName() != null ? eventRequest.getName() : "Agendamento da capela";
    }

    private String getEventDescription(EventRequest eventRequest) {
        if (eventRequest.getDescription() != null && !eventRequest.getDescription().isBlank()) {
            return eventRequest.getDescription();
        }

        StringBuilder descriptionBuilder = new StringBuilder();
        if (eventRequest.getMotivo() != null) {
            descriptionBuilder.append("Motivo: ").append(eventRequest.getMotivo()).append("\n");
        }
        if (eventRequest.getHorario() != null) {
            descriptionBuilder.append("Horário: ").append(eventRequest.getHorario()).append("\n");
        }
        if (eventRequest.getType() != null) {
            descriptionBuilder.append("Tipo: ").append(eventRequest.getType()).append("\n");
        }
        if (eventRequest.getEmail() != null) {
            descriptionBuilder.append("Email: ").append(eventRequest.getEmail()).append("\n");
        }
        if (eventRequest.getName() != null) {
            descriptionBuilder.append("Nome: ").append(eventRequest.getName());
        }
        return descriptionBuilder.toString().trim();
    }

    // UPDATE (Atualizar existente)
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
        return eventRepository.findById(id)
                .map(event -> {
                    event.setTitle(eventDetails.getTitle());
                    event.setDate(eventDetails.getDate());
                    event.setColor(eventDetails.getColor());
                    event.setDescription(eventDetails.getDescription());
                    if (eventDetails.getStatus() != null && !eventDetails.getStatus().isBlank()) {
                        event.setStatus(eventDetails.getStatus());
                    }
                    Event updatedEvent = eventRepository.save(event);
                    return ResponseEntity.ok(updatedEvent);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE (Remover do banco)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        return eventRepository.findById(id)
                .map(event -> {
                    eventRepository.delete(event);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}