package com.api.ecoreport.controller;

import com.api.ecoreport.model.Report;
import com.api.ecoreport.model.User;
import com.api.ecoreport.model.enums.ReportStatus;
import com.api.ecoreport.repository.ReportRepository;
import com.api.ecoreport.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReportController {

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/auth/report")
    public String handleFileUpload(@RequestParam("description") String description,
                                   @RequestParam("photoUrl") String photoUrl,
                                   @RequestParam("zipcode") String zipcode,
                                   RedirectAttributes redirectAttributes) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = null;

        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            user = userRepository.findByEmail(email).orElse(null);

            if (user != null) {
                // Set the userId if needed in the redirect attributes
                redirectAttributes.addFlashAttribute("userId", user.getId());
            } else {
                redirectAttributes.addFlashAttribute("message", "Usuário não encontrado.");
                return "redirect:/error"; // Redireciona para uma página de erro se o usuário não for encontrado
            }
        }

        Report report = new Report();
        report.setDescription(description);
        report.setPhotoUrl(photoUrl);
        report.setZipCode(zipcode);
        report.setStatus(ReportStatus.OPEN);
        report.setUser(user);

        reportRepository.save(report);

        redirectAttributes.addFlashAttribute("success", "Você realizou a denúncia com sucesso!");

        return "redirect:/home"; // Redireciona para a página inicial
    }
}
