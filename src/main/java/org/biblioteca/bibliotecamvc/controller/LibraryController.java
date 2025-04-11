package org.biblioteca.bibliotecamvc.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.biblioteca.bibliotecamvc.business.dto.BookDTO;
import org.biblioteca.bibliotecamvc.business.dto.LibraryDTO;
import org.biblioteca.bibliotecamvc.business.exception.library.LibraryNotFoundException;
import org.biblioteca.bibliotecamvc.business.service.BookService;
import org.biblioteca.bibliotecamvc.business.service.LibraryService;
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
    private BookService bookService;

    @GetMapping("/library/libraryMain")
    public String viewAdminView(Model model) {
        model.addAttribute("libraries", libraryService.findAll());
        return "/library/libraryMain";
    }

    @GetMapping("library/enter/enterLibraryMain/{id}")
    public String enterLibraryMain(Model model, @PathVariable Integer id, HttpSession session) {
        model.addAttribute("books", libraryService.getAllBooksById(id));
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
        libraryService.save(library);
        return "redirect:/library/libraryMain";
    }

    //TODO hacerlo desplegable para seleccionar el libro :)
    @GetMapping("/library/enter/addBook")
    public String addBook(Model model) {
        model.addAttribute("BookDTO", new BookDTO());
        model.addAttribute("libros", bookService.findAll());
        return "/library/enter/addBook";
    }

    @PostMapping("/library/enter/addBook")
    public String addBook(Model model, @ModelAttribute("BookDTO") BookDTO book, HttpSession session) {
        Integer libraryID = (Integer) session.getAttribute("LibraryID");
        libraryService.addBook(libraryID,book.getIsbn());
        model.addAttribute("books", libraryService.getAllBooksById(libraryID));
        return "redirect:/library/enter/enterLibraryMain/"+libraryID;
    }

    @GetMapping("/library/updateLibrary/{id}")
    public String updateLibraryForm(Model model, @PathVariable Integer id) {
        try {
            model.addAttribute("LibraryDTO", libraryService.findById(id));
        } catch (LibraryNotFoundException e) {
            System.err.println("No se ha encontrado la biblioteca");
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
            System.err.println("No se ha encontrado la biblioteca");
        }
        return "redirect:/library/libraryMain";
    }

    @GetMapping("/library/deleteBook/{id}")
    public String deleteLibraryEnter(@PathVariable String id, HttpSession session) {
        Integer libraryID = (Integer) session.getAttribute("LibraryID");
        try {
            libraryService.deleteBook(libraryID,id); //TODO change to quit book
        } catch (LibraryNotFoundException e) {
            System.err.println("No se ha encontrado la biblioteca");
        }
        return "redirect:/library/enter/enterLibraryMain/"+libraryID;
    }
}
