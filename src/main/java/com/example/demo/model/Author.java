package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "authors")
@Data
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private String name;

    @Column
    private String patronymic;

    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "image_url", nullable = true)
    private String imageUrl;

    public Author() {
    }

    public String getFullName(){
        return this.surname + " " + this.name+ " " + this.patronymic;
    }

}
