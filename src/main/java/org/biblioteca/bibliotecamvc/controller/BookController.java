package org.biblioteca.bibliotecamvc.controller;

import lombok.AllArgsConstructor;
import org.biblioteca.bibliotecamvc.business.dto.BookDTO;
import org.biblioteca.bibliotecamvc.business.exception.book.BookAlreadyExistsException;
import org.biblioteca.bibliotecamvc.business.exception.book.BookNotFoundException;
import org.biblioteca.bibliotecamvc.business.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class BookController {
    private BookService bookService;

    @GetMapping("/book/bookMain")
    public String viewAdminView(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "/book/bookMain";
    }

    @GetMapping("/book/saveBook")
    public String saveBook(Model model) {
        model.addAttribute("BookDTO", new BookDTO());
        return "/book/saveBook";
    }

    @PostMapping("/book/saveBook")
    public String saveBook(Model model, @ModelAttribute("BookDTO") BookDTO book) {
        model.addAttribute("BookDTO", new BookDTO());
        try {
            bookService.save(book);
        }catch (BookAlreadyExistsException e){
            System.err.println(e.getMessage());
        }

        return "redirect:/book/bookMain";
    }

    @GetMapping("/book/updateBook/{id}")
    public String updateBookForm(Model model, @PathVariable String id) {
        try {
            model.addAttribute("BookDTO", bookService.findById(id));
        } catch (BookNotFoundException e) {
            System.err.println("No se ha encontrado el libro");
            return "redirect:/book/bookMain";
        }

        return "/book/updateBook";
    }

    @PostMapping("/book/updateBook/{id}")
    public String updateBook(@ModelAttribute("AlumnoDTO") BookDTO bookDTO, @PathVariable String id) {
        try {
            bookService.update(bookDTO,id);
        } catch (BookNotFoundException e) {
            System.err.println("No se ha encontrado el libro");
        }

        return "redirect:/book/bookMain";
    }

    @GetMapping("/book/deleteBook/{id}")
    public String deleteBook(@PathVariable("id") String id) {
        try {
            bookService.delete(id);
        } catch (BookNotFoundException e) {
            System.err.println("No se ha encontrado el libro");
        }
        return "redirect:/book/bookMain";
    }
}
