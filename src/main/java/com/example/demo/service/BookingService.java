package com.example.demo.service;

import com.example.demo.model.Book;
import com.example.demo.model.Booking;
import com.example.demo.model.User;
import com.example.demo.model.Author;
import com.example.demo.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookService bookService;

    @Autowired
    public BookingService(BookingRepository bookingRepository, BookService bookService) {
        this.bookingRepository = bookingRepository;
        this.bookService = bookService;
    }

    public void createBooking(User user, Book book, Author author) {

        Booking booking = new Booking(user, book, author, LocalDateTime.now());
        bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() { return bookingRepository.findAll(); }

    public List<Booking> getBookingsByUser(User user) {
        return bookingRepository.findByUser(user);
    }
}
