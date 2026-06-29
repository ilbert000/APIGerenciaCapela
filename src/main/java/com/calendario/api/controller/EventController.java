package com.calendario.api.controller;

import com.calendario.api.model.Event;
import com.calendario.api.model.EventRequest;
import com.calendario.api.repository.EventRepository;
import com.calendario.api.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/eventos")
@CrossOrigin(origins = "*")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody EventRequest eventRequest) {
        Event event = new Event();
        String codigo = resolveCodigo(eventRequest);
        String status = resolveStatus(eventRequest);

        event.setTitle(getEventTitle(eventRequest, codigo));
        event.setDate(eventRequest.getDate());
        event.setColor(eventRequest.getColor() != null ? eventRequest.getColor() : "#6f25c8");
        event.setDescription(getEventDescription(eventRequest));
        event.setCodigo(codigo);
        event.setStatus(status);
        event.setContato(eventRequest.getContato());
        event.setTipoEvento(eventRequest.getTipoEvento());
        event.setUrgencia(eventRequest.getUrgencia());
        event.setQuantidade(eventRequest.getQuantidade());
        event.setHorarioInicio(eventRequest.getHorarioInicio());
        event.setHorarioFim(eventRequest.getHorarioFim());
        event.setObservacoes(eventRequest.getObservacoes());

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

        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
    }

    private String resolveCodigo(EventRequest eventRequest) {
        if (eventRequest.getCodigo() != null && !eventRequest.getCodigo().isBlank()) {
            return eventRequest.getCodigo();
        }

        List<Event> eventos = eventRepository.findAll();
        int nextNumber = eventos.stream()
                .map(Event::getCodigo)
                .filter(codigo -> codigo != null && codigo.startsWith("SOL-"))
                .map(codigo -> codigo.replace("SOL-", ""))
                .filter(value -> value.matches("\\d+"))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0) + 1;

        return "SOL-" + String.format("%04d", nextNumber);
    }

    private String resolveStatus(EventRequest eventRequest) {
        if (eventRequest.getStatus() != null && !eventRequest.getStatus().isBlank()) {
            return eventRequest.getStatus();
        }
        return "Aguardando Atendimento";
    }

    private String getEventTitle(EventRequest eventRequest, String codigo) {
        if (eventRequest.getTitle() != null && !eventRequest.getTitle().isBlank()) {
            return eventRequest.getTitle();
        }
        if (codigo != null && !codigo.isBlank()) {
            return "Solicitação " + codigo;
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
        if (eventRequest.getContato() != null) {
            descriptionBuilder.append("Contato: ").append(eventRequest.getContato()).append("\n");
        }
        if (eventRequest.getTipoEvento() != null) {
            descriptionBuilder.append("Tipo do evento: ").append(eventRequest.getTipoEvento()).append("\n");
        }
        if (eventRequest.getUrgencia() != null) {
            descriptionBuilder.append("Urgência: ").append(eventRequest.getUrgencia()).append("\n");
        }
        if (eventRequest.getQuantidade() != null) {
            descriptionBuilder.append("Quantidade: ").append(eventRequest.getQuantidade()).append("\n");
        }
        if (eventRequest.getHorarioInicio() != null) {
            descriptionBuilder.append("Horário inicial: ").append(eventRequest.getHorarioInicio()).append("\n");
        }
        if (eventRequest.getHorarioFim() != null) {
            descriptionBuilder.append("Horário final: ").append(eventRequest.getHorarioFim()).append("\n");
        }
        if (eventRequest.getObservacoes() != null) {
            descriptionBuilder.append("Observações: ").append(eventRequest.getObservacoes()).append("\n");
        }
        if (eventRequest.getName() != null) {
            descriptionBuilder.append("Nome: ").append(eventRequest.getName());
        }
        return descriptionBuilder.toString().trim();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
        return eventRepository.findById(id)
                .map(event -> {
                    event.setTitle(eventDetails.getTitle());
                    event.setDate(eventDetails.getDate());
                    event.setColor(eventDetails.getColor());
                    event.setDescription(eventDetails.getDescription());
                    event.setCodigo(eventDetails.getCodigo());
                    event.setStatus(eventDetails.getStatus());
                    event.setContato(eventDetails.getContato());
                    event.setTipoEvento(eventDetails.getTipoEvento());
                    event.setUrgencia(eventDetails.getUrgencia());
                    event.setQuantidade(eventDetails.getQuantidade());
                    event.setHorarioInicio(eventDetails.getHorarioInicio());
                    event.setHorarioFim(eventDetails.getHorarioFim());
                    event.setObservacoes(eventDetails.getObservacoes());
                    Event updatedEvent = eventRepository.save(event);
                    return ResponseEntity.ok(updatedEvent);
                })
                .orElse(ResponseEntity.notFound().build());
    }

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