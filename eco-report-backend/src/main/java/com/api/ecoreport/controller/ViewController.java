package com.api.ecoreport.controller;

import com.api.ecoreport.model.Report;
import com.api.ecoreport.model.User;
import com.api.ecoreport.repository.ReportRepository;
import com.api.ecoreport.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class ViewController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReportRepository reportRepository;

    @GetMapping("/home")
    public String showHomePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            userRepository.findByEmail(email).ifPresent(user -> {
                model.addAttribute("name", user.getName());
                model.addAttribute("neighborhood", user.getNeighborhood());

                List<Report> denuncias = reportRepository.findByUserId(user.getId());
                model.addAttribute("totalDenuncias", denuncias.size());
            });
        }
        return "home";
    }

    @GetMapping("/admin/dashboard")
    public String showAdminPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            userRepository.findByEmail(email).ifPresent(user -> model.addAttribute("name", user.getName()));
        }
        return "admin";
    }

    @GetMapping("/report")
    public String showReportPage(Model model) {
        return "report";
    }

    @GetMapping("/history")
    public String reportHistory(Model model, Principal principal) {
        String emailUser = principal.getName();
        Optional<User> userOptional = userRepository.findByEmail(emailUser);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            List<Report> denuncias = reportRepository.findByUserId(user.getId());
            model.addAttribute("denuncias", denuncias);
        }

        return "report-history";
    }

    @GetMapping("/info")
    public String showInfoPage() {
        return "info";
    }

}
