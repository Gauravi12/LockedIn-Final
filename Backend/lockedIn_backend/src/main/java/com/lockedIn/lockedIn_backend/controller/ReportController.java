package com.lockedIn.lockedIn_backend.controller;

import com.lockedIn.lockedIn_backend.dto.ReportResponse;
import com.lockedIn.lockedIn_backend.entity.User;
import com.lockedIn.lockedIn_backend.repository.UserRepo;
import com.lockedIn.lockedIn_backend.service.EmailService;
import com.lockedIn.lockedIn_backend.service.ReportService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;
    private final EmailService emailService;
    private final UserRepo userRepo;

    public ReportController(ReportService reportService, EmailService emailService, UserRepo userRepo) {
        this.reportService = reportService;
        this.emailService = emailService;
        this.userRepo = userRepo;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping("/get")
    public ReportResponse getReport() {
        User user = getCurrentUser();
        return reportService.getReport(user);
    }

    @GetMapping("/send")
    public String sendReport() {
        User user = getCurrentUser();
        emailService.sendReport(user);
        return "Report sent successfully!";
    }
}