package com.api.ecoreport.controller;

import com.api.ecoreport.model.User;
import com.api.ecoreport.model.enums.UserRole;
import com.api.ecoreport.repository.UserRepository;
import com.api.ecoreport.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Credenciais inválidas.");
        }
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String email, @RequestParam String password, @RequestParam String name,
                           @RequestParam String neighborhood, @RequestParam String role, RedirectAttributes redirectAttributes) {
        Optional<User> user = this.repository.findByEmail(email);

        if (user.isEmpty()) {
            authenticationService.registerUser(email, password, name, neighborhood, UserRole.valueOf(role));
            authenticationService.authenticateUser(email, password);

            redirectAttributes.addFlashAttribute("success", "Faça login para continuar.");
            return "redirect:/auth/login";
        }

        redirectAttributes.addFlashAttribute("error", "Email já está registrado.");
        return "register";
    }
}