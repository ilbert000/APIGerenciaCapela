package com.calendario.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EmailService {

    private static final String ADMIN_EMAIL = "admin@capela.gerencia";

    @Autowired
    private JavaMailSender mailSender;

    public void sendAdminNotification(String nome, String email, LocalDate data, String horario, String motivo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(ADMIN_EMAIL);
        message.setSubject("Nova solicitação de agendamento da capela");

        String body = String.format(
                "Uma nova solicitação de agendamento da capela foi recebida.%n%n" +
                "Nome: %s%n" +
                "Email: %s%n" +
                "Data: %s%n" +
                "Horário: %s%n" +
                "Motivo: %s%n%n" +
                "Por favor, acesse o sistema para aprovar ou recusar a solicitação.%n%n" +
                "Não responda este e-mail.",
                nome,
                email,
                data != null ? data.toString() : "",
                horario,
                motivo);

        message.setText(body);
        mailSender.send(message);
    }
}
