package com.example.demo.repository;

import com.example.demo.model.Book;
import com.example.demo.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitleContaining(String title);

    List<Book> findByAuthor(Author author);

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByQuantityGreaterThan(int quantity);
}