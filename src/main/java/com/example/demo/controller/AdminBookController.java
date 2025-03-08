package com.example.demo.controller;

import com.example.demo.model.Author;
import com.example.demo.model.Book;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.service.BookService;
import com.example.demo.model.Booking;
import com.example.demo.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminBookController {

    private final BookService bookService;
    private final BookingService bookingService;
    private final AuthorRepository authorRepository;

    private static final String UPLOAD_DIR = "C:/Users/admin/Desktop/library/uploads/";

    @Autowired
    public AdminBookController(BookService bookService,BookingService bookingService, AuthorRepository authorRepository) {
        this.bookService = bookService;
        this.bookingService = bookingService;
        this.authorRepository = authorRepository;
    }

    @GetMapping("/books")
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "admin/list-books";
    }

    @GetMapping("/books/search")
    public String searchBooks(@RequestParam(value = "title", required = false) String title,
                              @ModelAttribute("author") Author author,
                              @RequestParam(value = "isbn", required = false) String isbn,
                              Model model) {
        List<Book> books = new ArrayList<>();

        if (isbn != null && !isbn.trim().isEmpty()) {
            Optional<Book> bookOpt = bookService.findBookByIsbn(isbn.trim());
            bookOpt.ifPresent(books::add);
        }
        else if (author.getSurname() != null && !author.getSurname().trim().isEmpty()) {
            books = bookService.findBooksByAuthor(author.getSurname().trim());
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

    @GetMapping("/books/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authorRepository.findAll());
        return "admin/add-book";
    }

    @PostMapping("/books/add")
    public String addBook(@ModelAttribute("book") Book book) {
        Author author = authorRepository.findById(book.getAuthor().getId())
                .orElseThrow(() -> new IllegalArgumentException("Автор не найден"));
        book.setAuthor(author);
        bookService.addBook(book);
        return "redirect:/admin/books";
    }

    @GetMapping("/books/edit/{id}")
    public String showEditBookForm(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        return "admin/edit-book";
    }

    @PostMapping("/books/edit/{id}")
    public String updateBook(@PathVariable Long id, @Valid @ModelAttribute("book") Book bookDetails, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/edit-book";
        }
        bookService.updateBook(id, bookDetails);
        return "redirect:/admin/books";
    }

    @GetMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/admin/books";
    }

    @GetMapping("/books/bookings")
    public String listUserBookings(Model model) {
        List<Booking> bookings = bookingService.getAllBookings();
        model.addAttribute("bookings", bookings);
        return "admin/user-bookings";
    }

    @GetMapping("/authors")
    public String listAuthors(Model model) {
        model.addAttribute("authors", authorRepository.findAll());
        return "admin/authors";
    }

    @GetMapping("/authors/add")
    public String showAddAuthorForm(Model model) {
        model.addAttribute("author", new Author());
        return "admin/add-author";
    }

    @PostMapping("/authors/add")
    public String addAuthor(@Valid @ModelAttribute("author") Author author, @RequestParam("imageFile") MultipartFile imageFile, Model model) {
        if (author.getSurname() == null || author.getSurname().trim().isEmpty() ||
                author.getName() == null || author.getName().trim().isEmpty()) {
            model.addAttribute("errorMessage", "Фамилия и имя автора не могут быть пустыми");
            return "admin/add-author";
        }

        if (!imageFile.isEmpty()) {
            String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            File destinationFile = new File(UPLOAD_DIR + fileName);
            try {
                imageFile.transferTo(destinationFile);
                author.setImageUrl("/uploads/" + fileName);
            } catch (IOException e) {
                model.addAttribute("errorMessage", "Ошибка загрузки файла: " + e.getMessage());
                return "admin/add-author";
            }
        } else {
            author.setImageUrl("");
        }

        authorRepository.save(author);
        return "redirect:/admin/authors";
    }

    @GetMapping("/authors/edit/{id}")
    public String showEditAuthorForm(@PathVariable Long id, Model model) {
        Optional<Author> authorOpt = authorRepository.findById(id);
        if (authorOpt.isEmpty()) {
            return "redirect:/admin/authors";
        }
        model.addAttribute("author", authorOpt.get());
        return "admin/edit-author";
    }

    @PostMapping("/authors/edit/{id}")
    public String updateAuthor(@PathVariable Long id, @Valid @ModelAttribute("author") Author authorDetails, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/edit-author";
        }
        Author author = authorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Автор не найден"));
        author.setName(authorDetails.getName());
        author.setSurname(authorDetails.getSurname());
        author.setSurname(authorDetails.getPatronymic());
        authorRepository.save(author);
        return "redirect:/admin/authors";
    }

    @GetMapping("/authors/delete/{id}")
    public String deleteAuthor(@PathVariable Long id) {
        authorRepository.deleteById(id);
        return "redirect:/admin/authors";
    }
}
