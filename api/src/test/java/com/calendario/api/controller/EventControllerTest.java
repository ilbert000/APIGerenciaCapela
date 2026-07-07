package com.calendario.api.controller;

import com.calendario.api.model.Event;
import com.calendario.api.repository.EventRepository;
import com.calendario.api.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private EmailService emailService;

    @Test
    void shouldReturnStatusWhenCreatingEvent() throws Exception {
        Event savedEvent = new Event();
        savedEvent.setId(1L);
        savedEvent.setTitle("Nome do solicitante");
        savedEvent.setDate(LocalDate.of(2026, 7, 7));
        savedEvent.setColor("#ef4444");
        savedEvent.setDescription("Descrição do evento");
        savedEvent.setStatus("Aguardando Atendimento");

        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);

        mockMvc.perform(post("/api/eventos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Maria",
                                  "email": "maria@email.com",
                                  "date": "2026-07-07",
                                  "color": "#ef4444",
                                  "description": "Descrição do evento",
                                  "status": "Aguardando Atendimento"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("Aguardando Atendimento"));
    }
}
