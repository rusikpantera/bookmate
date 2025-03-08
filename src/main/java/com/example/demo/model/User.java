package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column
    private String middlename;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean emailVerified = false;

    private String confirmationToken;

    private LocalDateTime tokenExpiryDate;


    public User() {
    }

    public User(String username, String password, String role, String firstname, String lastname, String middlename,
                String email, boolean emailVerified, String confirmationToken, LocalDateTime tokenExpiryDate) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.firstname = firstname;
        this.lastname = lastname;
        this.middlename = middlename;
        this.email = email;
        this.emailVerified = emailVerified;
        this.confirmationToken = confirmationToken;
        this.tokenExpiryDate = tokenExpiryDate;
    }

    public String getFullName(){
        return this.firstname + " " + this.lastname+ " " + this.middlename;
    }

}
