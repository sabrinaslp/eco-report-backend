package com.api.ecoreport.controller;

import com.api.ecoreport.model.Report;
import com.api.ecoreport.model.enums.ReportStatus;
import com.api.ecoreport.services.ReportService;
import com.api.ecoreport.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class ViewController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReportService reportService;

    @GetMapping("/home")
    public String showHomePage(Model model) {
        userService.findAuthenticatedUser().ifPresent(user -> {
            model.addAttribute("name", user.getName());
            model.addAttribute("neighborhood", user.getNeighborhood());

            List<Report> denuncias = reportService.findReportsByUserId(user.getId());
            model.addAttribute("totalDenuncias", denuncias.size());
        });
        return "home";
    }

    @GetMapping("/admin/dashboard")
    public String showAdminPage(@RequestParam(value = "status", required = false) String status, Model model) {
        userService.findAuthenticatedUser().ifPresent(user -> model.addAttribute("name", user.getName()));

        List<Report> denuncias;
        if (status != null && !status.isEmpty()) {
            denuncias = reportService.findReportsByStatus(ReportStatus.valueOf(status));
        } else {
            denuncias = reportService.findAllReports();
        }

        model.addAttribute("denuncias", denuncias);
        model.addAttribute("status", status);

        return "admin";
    }

    @PostMapping("/admin/updateStatus")
    public String updateReportStatus(@RequestParam("denunciaId") Long denunciaId,
                                     @RequestParam("status") String status,
                                     RedirectAttributes redirectAttributes) {
        Report report = reportService.findReportById(denunciaId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid report Id:" + denunciaId));
        reportService.updateReportStatus(report, ReportStatus.valueOf(status));

        redirectAttributes.addFlashAttribute("message", "Status da denúncia alterado com sucesso!");

        return "redirect:/admin/dashboard";
    }

    @GetMapping("/report")
    public String showReportPage(Model model) {
        return "report";
    }

    @GetMapping("/history")
    public String reportHistory(Model model, Principal principal) {
        userService.findUserByEmail(principal.getName()).ifPresent(user -> {
            List<Report> denuncias = reportService.findReportsByUserId(user.getId());
            model.addAttribute("denuncias", denuncias);
        });
        return "report-history";
    }

    @GetMapping("/info")
    public String showInfoPage() {
        return "info";
    }
}