package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;
import java.time.LocalDateTime;

@Controller
public class RegistrationController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Autowired
    public RegistrationController(UserService userService, UserRepository userRepository, EmailService emailService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                model.addAttribute("errorMessage", "Email не может быть пустым");
                return "registration";
            }

            if (user.getRole() == null || user.getRole().trim().isEmpty()) {
                user.setRole("ROLE_USER");
            }

            String token = UUID.randomUUID().toString();
            user.setConfirmationToken(token);
            user.setTokenExpiryDate(LocalDateTime.now().plusHours(24));
            user.setEmailVerified(false);

            userService.registerUser(user.getUsername(), user.getPassword(),
                    user.getFirstname(), user.getLastname(), user.getMiddlename(), user.getEmail(), user.isEmailVerified(),
                    user.getConfirmationToken(), user.getTokenExpiryDate());

            emailService.sendConfirmationEmail(user.getEmail(), token);

            return "login";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "registration";
        }
    }

    @GetMapping("/confirm")
    public String confirmRegistration(@RequestParam("token") String token, Model model) {
        System.out.println("Получен токен: " + token);
        Optional<User> optionalUser = userRepository.findByConfirmationToken(token);
        if (optionalUser.isEmpty()) {
            model.addAttribute("message", "Неверный токен подтверждения.");
            return "error";
        }

        User user = optionalUser.get();
        System.out.println("Найден пользователь: " + user.getUsername() + ", emailVerified = " + user.isEmailVerified());

        if (user.getTokenExpiryDate() == null || user.getTokenExpiryDate().isBefore(LocalDateTime.now())) {
            model.addAttribute("message", "Срок действия токена истёк.");
            return "error";
        }

        user.setEmailVerified(true);
        user.setConfirmationToken(null);
        user.setTokenExpiryDate(null);
        userRepository.save(user);

        System.out.println("Пользователь " + user.getUsername() + " подтверждён, emailVerified = " + user.isEmailVerified());

        return "confirm";
    }
}

