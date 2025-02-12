package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

    @GetMapping("/default")
    public String defaultAfterLogin(Authentication authentication) {
        if (authentication.getAuthorities().stream().anyMatch(
                auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin/books";
        }
        else if (authentication.getAuthorities().stream().anyMatch(
                auth -> auth.getAuthority().equals("ROLE_USER"))) {
            return "redirect:/user/books";
        }
        return "redirect:/login?error";
    }
}
