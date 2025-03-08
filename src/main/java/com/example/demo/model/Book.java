package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "books")
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(name = "published_date")
    private String publishedDate;

    @Column(nullable = false)
    private int quantity;

    public Book() {
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + (author != null ? author.getFullName() : "N/A") + '\'' +
                ", isbn='" + isbn + '\'' +
                ", publishedDate=" + publishedDate +
                ", quantity=" + quantity +
                '}';
    }
}