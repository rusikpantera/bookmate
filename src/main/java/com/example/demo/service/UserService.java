package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    public void registerUser(String username, String rawPassword, String firstname, String lastname, String middlename,
                             String email, boolean phoneNumber, String confirmationToken, LocalDateTime tokenExpiryDate) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Пользователь с таким логином уже существует");
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);

        User user = new User(username, encodedPassword, "ROLE_USER", firstname, lastname , middlename, email,
                phoneNumber, confirmationToken, tokenExpiryDate);
        userRepository.save(user);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь с username " + username + " не найдена"));
    }
}
