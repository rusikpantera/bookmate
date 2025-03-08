package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("Пользователь с именем " + username + " не найден");
        }

        User user = optionalUser.get();

        if (!user.isEmailVerified()) {
            throw new DisabledException("Email не подтвержден. Проверьте почту для подтверждения регистрации.");
        }

        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            throw new UsernameNotFoundException("Роль не установлена для пользователя " + username);
        }

        String roleWithoutPrefix = user.getRole().replace("ROLE_", "");
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(roleWithoutPrefix)
                .build();
    }

}
