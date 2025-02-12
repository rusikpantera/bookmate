package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import com.example.demo.model.Booking;
import com.example.demo.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/books")
public class AdminBookController {

    private final BookService bookService;
    private final BookingService bookingService;

    @Autowired
    public AdminBookController(BookService bookService,BookingService bookingService) {
        this.bookService = bookService;
        this.bookingService = bookingService;
    }

    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "admin/list-books";
    }

    @GetMapping("/search")
    public String searchBooks(@RequestParam(value = "title", required = false) String title,
                              @RequestParam(value = "author", required = false) String author,
                              @RequestParam(value = "isbn", required = false) String isbn,
                              Model model) {
        List<Book> books = new ArrayList<>();

        if (isbn != null && !isbn.trim().isEmpty()) {
            Optional<Book> bookOpt = bookService.findBookByIsbn(isbn.trim());
            bookOpt.ifPresent(books::add);
        }
        else if (author != null && !author.trim().isEmpty()) {
            books = bookService.findBooksByAuthor(author.trim());
        }
        else if (title != null && !title.trim().isEmpty()) {
            books = bookService.findBooksByTitle(title.trim());
        }
        else {
            books = bookService.getAllBooks();
        }

        model.addAttribute("books", books);
        return "admin/list-books";
    }

    @GetMapping("/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "admin/add-book";
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute("book") Book book) {
        bookService.addBook(book);
        return "redirect:/admin/books";
    }

    @GetMapping("/edit/{id}")
    public String showEditBookForm(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        return "admin/edit-book";
    }

    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable Long id, @Valid @ModelAttribute("book") Book bookDetails, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "admin/edit-book";
        }
        bookService.updateBook(id, bookDetails);
        return "redirect:/admin/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/admin/books";
    }

    @GetMapping("/bookings")
    public String listUserBookings(Model model) {
        List<Booking> bookings = bookingService.getAllBookings();
        model.addAttribute("bookings", bookings);
        return "admin/user-bookings";
    }
}
