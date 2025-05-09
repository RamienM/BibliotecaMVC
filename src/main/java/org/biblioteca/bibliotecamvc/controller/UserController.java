package org.biblioteca.bibliotecamvc.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.biblioteca.bibliotecamvc.business.dto.UserRegisterDTO;
import org.biblioteca.bibliotecamvc.business.exception.book.BookIsBookedException;
import org.biblioteca.bibliotecamvc.business.exception.borrow.BorrowNotFoundException;
import org.biblioteca.bibliotecamvc.business.exception.log.LogNotFoundException;
import org.biblioteca.bibliotecamvc.business.exception.user.UserAlreadyBookedThisBookException;
import org.biblioteca.bibliotecamvc.business.exception.user.UserAlreadyExistsException;
import org.biblioteca.bibliotecamvc.business.exception.user.UserNotFoundException;
import org.biblioteca.bibliotecamvc.business.service.BookService;
import org.biblioteca.bibliotecamvc.business.service.LibraryService;
import org.biblioteca.bibliotecamvc.business.service.LogService;
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
    private final LogService logService;
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
        try {
            userService.register(register);
        }catch (UserAlreadyExistsException e){
            System.err.println(e.getMessage());
            return "/user/admin/saveUser";
        }
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
        } catch (UserNotFoundException e) {
            System.err.println(e.getMessage());
        }

        return "redirect:/user/admin/userAdminMain";
    }

    @GetMapping("/user/admin/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") String id) {
        try {
            userService.delete(id);
        } catch (UserNotFoundException e) {
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
        } catch (UserNotFoundException e) {
            System.err.println(e.getMessage());
        }

        return "redirect:/user/user/userMain";
    }

    @GetMapping("user/borrow/book/{id}")
    public String borrowBook(@PathVariable("id") String id, HttpSession session) {
        Integer userID = (Integer) session.getAttribute("ActualUser");
        Integer libraryId = (Integer) session.getAttribute("LibraryID");
        try {
            libraryService.borrowBook(libraryId,id,userID);
        }catch (LogNotFoundException | UserNotFoundException | BookIsBookedException | UserAlreadyBookedThisBookException e){
            System.err.println(e.getMessage());
        }

        return "redirect:/user/user/userBooksLibrary/"+libraryId;
    }

    @GetMapping("user/return/book/{id}")
    public String returnBook(@PathVariable("id") String id, HttpSession session) {
        Integer userID = (Integer) session.getAttribute("ActualUser");
        Integer libraryId = (Integer) session.getAttribute("LibraryID");
        try {
            libraryService.returnBook(libraryId,id, userID);
        }catch (LogNotFoundException | BorrowNotFoundException e){
            System.err.println(e.getMessage());
        }

        return "redirect:/user/user/userMain";
    }

    @GetMapping("/user/user/userBooksLibrary/{id}")
    public String enterLibraryMain(Model model, @PathVariable Integer id, HttpSession session) {
        model.addAttribute("books", logService.getAllBooksAvailableByLibraryId(id));
        session.setAttribute("LibraryID",id);
        return "/user/user/userBooksLibrary";
    }

    @GetMapping("/user/user/userLog")
    public String userLog(Model model, HttpSession session) {
        Integer userID = (Integer) session.getAttribute("ActualUser");
        model.addAttribute("logs", logService.getAllLogsByUserId(userID));
        return "/user/user/userLog";
    }

}
