package com.api.ecoreport.services;

import com.api.ecoreport.model.Report;
import com.api.ecoreport.model.User;
import com.api.ecoreport.model.enums.ReportStatus;
import com.api.ecoreport.repository.ReportRepository;
import com.api.ecoreport.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    public void createNewReport(String description, String photoUrl, String zipcode, RedirectAttributes redirectAttributes) {
        User user = getAuthenticatedUser();

        if (user != null) {
            Report report = new Report();
            report.setDescription(description);
            report.setPhotoUrl(photoUrl);
            report.setZipCode(zipcode);
            report.setStatus(ReportStatus.OPEN);
            report.setUser(user);

            reportRepository.save(report);
            redirectAttributes.addFlashAttribute("success", "Você realizou a denúncia com sucesso!");
            redirectAttributes.addFlashAttribute("userId", user.getId());
        } else {
            redirectAttributes.addFlashAttribute("message", "Usuário não encontrado.");
            throw new RuntimeException("Usuário não autenticado.");
        }
    }

    public List<Report> findReportsByUserId(Long userId) {
        return reportRepository.findByUserId(userId);
    }

    public List<Report> findAllReports() {
        return reportRepository.findAll();
    }

    public List<Report> findReportsByStatus(ReportStatus status) {
        return reportRepository.findByStatus(status);
    }

    public Optional<Report> findReportById(Long id) {
        return reportRepository.findById(id);
    }

    public void updateReportStatus(Report report, ReportStatus status) {
        report.setStatus(status);
        reportRepository.save(report);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            return userRepository.findByEmail(email).orElse(null);
        }
        return null;
    }
}