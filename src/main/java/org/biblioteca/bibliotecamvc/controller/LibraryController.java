package org.biblioteca.bibliotecamvc.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.biblioteca.bibliotecamvc.business.dto.BookDTO;
import org.biblioteca.bibliotecamvc.business.dto.LibraryDTO;
import org.biblioteca.bibliotecamvc.business.exception.book.BookNotFoundException;
import org.biblioteca.bibliotecamvc.business.exception.library.LibraryAlreadyExistException;
import org.biblioteca.bibliotecamvc.business.exception.library.LibraryAlreadyHaveThatBook;
import org.biblioteca.bibliotecamvc.business.exception.library.LibraryNotFoundException;
import org.biblioteca.bibliotecamvc.business.exception.log.LogNotFoundException;
import org.biblioteca.bibliotecamvc.business.service.BookService;
import org.biblioteca.bibliotecamvc.business.service.LibraryService;
import org.biblioteca.bibliotecamvc.business.service.LogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class LibraryController {
    private LibraryService libraryService;
    private LogService logService;
    private BookService bookService;

    @GetMapping("/library/libraryMain")
    public String viewAdminView(Model model) {
        model.addAttribute("libraries", libraryService.findAll());
        return "/library/libraryMain";
    }

    @GetMapping("/library/enter/enterLibraryMain/{id}")
    public String enterLibraryMain(Model model, @PathVariable Integer id, HttpSession session) {
        model.addAttribute("books", logService.getAllBooksAvailableByLibraryId(id));
        session.setAttribute("LibraryID",id);
        return "/library/enter/enterLibraryMain";
    }

    @GetMapping("/library/saveLibrary")
    public String saveLibrary(Model model) {
        model.addAttribute("LibraryDTO", new LibraryDTO());
        return "/library/saveLibrary";
    }

    @PostMapping("/library/saveLibrary")
    public String saveLibrary(Model model, @ModelAttribute("LibraryDTO") LibraryDTO library) {
        model.addAttribute("LibraryDTO", new LibraryDTO());
        try {
            libraryService.save(library);
        }catch (LibraryAlreadyExistException e){
            System.err.println(e.getMessage());
        }

        return "redirect:/library/libraryMain";
    }

    @GetMapping("/library/enter/addBook")
    public String addBook(Model model) {
        model.addAttribute("BookDTO", new BookDTO());
        model.addAttribute("libros", bookService.findAll());
        return "/library/enter/addBook";
    }

    @PostMapping("/library/enter/addBook")
    public String addBook(@ModelAttribute("BookDTO") BookDTO book, HttpSession session) {
        Integer libraryID = (Integer) session.getAttribute("LibraryID");
        try {
            libraryService.addBook(libraryID,book.getIsbn());
        }catch (LibraryNotFoundException | BookNotFoundException | LibraryAlreadyHaveThatBook e){
            System.err.println(e.getMessage());
        }

        return "redirect:/library/enter/enterLibraryMain/"+libraryID;
    }

    @GetMapping("/library/updateLibrary/{id}")
    public String updateLibraryForm(Model model, @PathVariable Integer id) {
        try {
            model.addAttribute("LibraryDTO", libraryService.findById(id));
        } catch (LibraryNotFoundException e) {
            System.err.println("No se ha encontrado la biblioteca");
            model.addAttribute("LibraryDTO", new LibraryDTO());
        }

        return "/library/updateLibrary";
    }

    @PostMapping("/library/updateLibrary/{id}")
    public String updateLibrary(@ModelAttribute("LibraryDTO") LibraryDTO libraryDTO, @PathVariable Integer id) {
        try {
            libraryService.update(libraryDTO,id);
        } catch (LibraryNotFoundException e) {
            System.err.println("No se ha encontrado la biblioteca");
        }

        return "redirect:/library/libraryMain";
    }

    @GetMapping("/library/deleteLibrary/{id}")
    public String deleteLibrary(@PathVariable("id") Integer id) {
        try {
            libraryService.delete(id);
        } catch (LibraryNotFoundException e) {
            System.err.println(e.getMessage());
        }
        return "redirect:/library/libraryMain";
    }

    @GetMapping("/library/deleteBook/{id}")
    public String deleteLibraryEnter(@PathVariable String id, HttpSession session) {
        Integer libraryID = (Integer) session.getAttribute("LibraryID");
        try {
            libraryService.deleteBook(libraryID,id);
        } catch (LogNotFoundException e) {
            System.err.println(e.getMessage());
        }
        return "redirect:/library/enter/enterLibraryMain/"+libraryID;
    }
}
