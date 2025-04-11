package org.biblioteca.bibliotecamvc.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.biblioteca.bibliotecamvc.business.dto.LibraryDTO;
import org.biblioteca.bibliotecamvc.business.dto.UserRegisterDTO;
import org.biblioteca.bibliotecamvc.business.exception.book.BookNotFoundException;
import org.biblioteca.bibliotecamvc.business.service.BookService;
import org.biblioteca.bibliotecamvc.business.service.LibraryService;
import org.biblioteca.bibliotecamvc.business.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final BookService bookService;
    private final LibraryService libraryService;

    @GetMapping("/user/admin/userAdminMain")
    public String viewAdminView(Model model) {
        model.addAttribute("users", userService.findAll());
        return "/user/admin/userAdminMain";
    }

    @GetMapping("/user/admin/saveUser")
    public String saveUserAdminMain(Model model) {
        model.addAttribute("UserRegisterDTO", new UserRegisterDTO());
        return "/user/admin/saveUser";
    }
    @PostMapping("/user/admin/saveUser")
    public String saveUserAdminMain(@ModelAttribute("UserRegisterDTO") UserRegisterDTO register){
        userService.register(register);
        return "redirect:/user/admin/userAdminMain";
    }

    @GetMapping("/user/admin/updateUser/{id}")
    public String updateUserAdminMain(Model model, @PathVariable Integer id, HttpSession session) {
        model.addAttribute("UserRegisterDTO", new UserRegisterDTO());
        session.setAttribute("UserID",id);
        return "/user/admin/updateUser";
    }

    @PostMapping("/user/admin/updateUser")
    public String updateUserAdminMain(@ModelAttribute("UserRegisterDTO") UserRegisterDTO userDTO, HttpSession session){
        try {
            Integer userID = (Integer) session.getAttribute("UserID");
            userService.update(userDTO, userID);
        } catch (BookNotFoundException e) {
            System.err.println("No se ha encontrado el libro");
        }

        return "redirect:/user/admin/userAdminMain";
    }

    @GetMapping("/user/admin/deleteUser/{id}")
    public String deleteBook(@PathVariable("id") String id) {
        try {
            userService.delete(id);
        } catch (BookNotFoundException e) {
            System.err.println("No se ha encontrado el libro");
        }
        return "redirect:/user/admin/userAdminMain";
    }


    /// User
    @GetMapping("/user/user/userMain")
    public String viewUserView(Model model, HttpSession session) {
        Integer userID = (Integer) session.getAttribute("ActualUser");
        model.addAttribute("ActualUser",userID);
        model.addAttribute("books", bookService.getAllBooksByUserId(userID)); //Hacer que busque por usuario en un futuro
        return "/user/user/userMain";
    }

    @GetMapping("/user/user/userLibraries")
    public String viewLibraryView(Model model) {
        model.addAttribute("libraries", libraryService.findAll());
        return "/user/user/userLibraries";
    }

    @GetMapping("/user/user/updateUser")
    public String updateUserMain(Model model) {
        model.addAttribute("UserRegisterDTO", new UserRegisterDTO());
        return "/user/user/updateUser";
    }

    @PostMapping("/user/user/updateUser")
    public String updateUserMain(@ModelAttribute("UserRegisterDTO") UserRegisterDTO userDTO, HttpSession session){
        try {
            Integer userID = (Integer) session.getAttribute("ActualUser");
            userService.update(userDTO, userID);
        } catch (BookNotFoundException e) {
            System.err.println("No se ha encontrado el libro");
        }

        return "redirect:/user/user/userMain";
    }

    @GetMapping("user/borrow/book/{id}")
    public String borrowBook(@PathVariable("id") String id, HttpSession session) {
        Integer userID = (Integer) session.getAttribute("ActualUser");
        Integer libraryiD = (Integer) session.getAttribute("LibraryID");
        userService.borrowBook(id, userID);
        return "redirect:/user/user/userBooksLibrary/"+libraryiD;
    }

    @GetMapping("user/return/book/{id}")
    public String returnBook(@PathVariable("id") String id, HttpSession session) {
        Integer userID = (Integer) session.getAttribute("ActualUser");
        userService.returnBook(id, userID);
        return "redirect:/user/user/userMain";
    }

    @GetMapping("/user/user/userBooksLibrary/{id}")
    public String enterLibraryMain(Model model, @PathVariable Integer id, HttpSession session) {
        model.addAttribute("books", libraryService.getAllBooksAvailableById(id));
        session.setAttribute("LibraryID",id);
        return "/user/user/userBooksLibrary";
    }

    @GetMapping("/user/user/userLog")
    public String userLog(Model model, HttpSession session) {
        Integer userID = (Integer) session.getAttribute("ActualUser");
        model.addAttribute("books", bookService.getLog(userID));
        return "/user/user/userLog";
    }

}
