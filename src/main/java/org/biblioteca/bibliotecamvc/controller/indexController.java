package org.biblioteca.bibliotecamvc.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.biblioteca.bibliotecamvc.business.dto.UserDTO;
import org.biblioteca.bibliotecamvc.business.dto.UserRegisterDTO;
import org.biblioteca.bibliotecamvc.business.exception.user.PasswordNotMatchException;
import org.biblioteca.bibliotecamvc.business.exception.user.UserNotFoundException;
import org.biblioteca.bibliotecamvc.business.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class indexController {
    private UserService userService;

    @GetMapping("/main")
    public String index() {
        return "index";
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("UserRegisterDTO", new UserRegisterDTO());
        return "/auth/login";
    }
    @GetMapping("/form-register")
    public String register(Model model) {
        model.addAttribute("UserRegisterDTO", new UserRegisterDTO());
        return "/auth/register";
    }
    @PostMapping("/auth/register")
    public String registro(@ModelAttribute("UserRegisterDTO") UserRegisterDTO register){
        userService.register(register);
        return "redirect:/";
    }
    @PostMapping("/auth/login")
    public String login(@ModelAttribute("UserRegisterDTO") UserRegisterDTO inicio, HttpSession session) {
        try {
            UserDTO user = userService.login(inicio);
            session.setAttribute("ActualUser", user.getId());
            if (user.getAdmin()) return "redirect:/main";
            else return "redirect:/user/user/userMain";


        }catch (PasswordNotMatchException | UserNotFoundException e) {
            System.err.println(e.getMessage());
        }
        return "redirect:/";
    }
}
