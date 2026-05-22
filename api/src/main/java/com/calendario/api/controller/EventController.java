package com.calendario.api.controller;

import com.calendario.api.model.Event;
import com.calendario.api.repository.EventRepository;
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

    // READ ALL (Listar todos)
    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // CREATE (Inserir novo)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Event createEvent(@RequestBody Event event) {
        return eventRepository.save(event);
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