package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String bookTitle;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false)
    private int quantity;

    @Column
    private LocalDate publishedDate;

    @Column(nullable = false)
    private LocalDateTime bookingDate;

    public Booking() {
    }

    public Booking(String username, String bookTitle, String author, String isbn,LocalDate publishedDate, int quantity, LocalDateTime bookingDate) {
        this.username = username;
        this.bookTitle = bookTitle;
        this.author = author;
        this.isbn = isbn;
        this.quantity = quantity;
        this.publishedDate = publishedDate;
        this.bookingDate = bookingDate;
    }
}
