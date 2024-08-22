package com.api.ecoreport.controller;

import com.api.ecoreport.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/auth/report")
    public String createNewReport(@RequestParam("description") String description,
                                  @RequestParam("photoUrl") String photoUrl,
                                  @RequestParam("zipcode") String zipcode,
                                  RedirectAttributes redirectAttributes) {

        try {
            reportService.createNewReport(description, photoUrl, zipcode, redirectAttributes);
            return "redirect:/home";

        } catch (RuntimeException e) {
            return "redirect:/error";
        }
    }
}