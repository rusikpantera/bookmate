package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.model.Booking;
import com.example.demo.service.BookService;
import com.example.demo.model.User;
import com.example.demo.service.BookingService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user/books")
public class UserBookController {

    private final BookService bookService;
    private final BookingService bookingService;
    private final UserService userService;

    @Autowired
    public UserBookController(BookService bookService, BookingService bookingService, UserService userService) {
        this.bookService = bookService;
        this.bookingService = bookingService;
        this.userService = userService;
    }


    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.getAvailableBooks());
        return "user/list-books";
    }

    @GetMapping("/search")
    public String searchBooks(@RequestParam(value = "title", required = false) String title,
                              @RequestParam(value = "author", required = false) String author,
                              @RequestParam(value = "isbn", required = false) String isbn,
                              Model model) {
        List<Book> books = new ArrayList<>();

        if (isbn != null && !isbn.trim().isEmpty()) {
            Optional<Book> bookOpt = bookService.findBookByIsbn(isbn.trim());
            if (bookOpt.isPresent() && bookOpt.get().getQuantity() > 0) {
                books.add(bookOpt.get());
            }
        }
        else if (author != null && !author.trim().isEmpty()) {
            books = bookService.findBooksByAuthor(author.trim());
            books.removeIf(book -> book.getQuantity() <= 0);
        }
        else if (title != null && !title.trim().isEmpty()) {
            books = bookService.findBooksByTitle(title.trim());
            books.removeIf(book -> book.getQuantity() <= 0);
        }
        else {
            books = bookService.getAvailableBooks();
        }

        model.addAttribute("books", books);
        return "user/list-books";
    }

    @GetMapping("/book/{id}")
    public String showBookingForm(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        return "user/book";
    }

    @PostMapping("/book/{id}")
    public String bookBook(@PathVariable Long id,
                           @RequestParam("quantity") int bookingQuantity,
                           Model model) {
        Book book = bookService.getBookById(id);

        if (book.getQuantity() < bookingQuantity) {
            model.addAttribute("book", book);
            model.addAttribute("errorMessage", "Запрошенное количество превышает доступное!");
            return "user/book";
        }

        book.setQuantity(book.getQuantity() - bookingQuantity);
        bookService.updateBook(id, book);

        User user = userService.getCurrentUser();

        bookingService.createBooking(user, book, book.getAuthor());

        model.addAttribute("book", book);
        model.addAttribute("message", "Книга успешно забронирована!");
        return "user/booking-success";
    }

    @GetMapping("/myBookings")
    public String viewMyBookings(Model model) {
        User user = userService.getCurrentUser();
        List<Booking> bookings = bookingService.getBookingsByUser(user);
        model.addAttribute("bookings", bookings);
        return "user/my-bookings";
    }
}
