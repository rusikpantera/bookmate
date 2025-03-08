package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendConfirmationEmail(String to, String token) {
        String subject = "Подтверждение регистрации";
        String confirmationUrl = "http://localhost:8080/confirm?token=" + token;
        String message = "Для завершения регистрации, пожалуйста, перейдите по следующей ссылке:\n" + confirmationUrl;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        email.setFrom("salikhov.rusikk@yandex.ru");

        mailSender.send(email);
    }
}
