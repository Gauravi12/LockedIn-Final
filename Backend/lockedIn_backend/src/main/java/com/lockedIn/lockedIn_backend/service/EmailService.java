package com.lockedIn.lockedIn_backend.service;

import com.lockedIn.lockedIn_backend.dto.ReportResponse;
import com.lockedIn.lockedIn_backend.entity.User;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final ReportService reportService;
    private final EmailTemplateService emailTemplateService;

    @Value("${spring.mail.username}")
    private String fromMail;

    public EmailService(JavaMailSender mailSender ,EmailTemplateService emailTemplateService, ReportService reportService){
        this.mailSender = mailSender;
        this.emailTemplateService = emailTemplateService;
        this.reportService = reportService;
    }

    public void sendReport(User user){
        try{
            ReportResponse response = reportService.getReport(user);
            String html = emailTemplateService.buildReportHtml(response);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message , true , "UTF-8");
            helper.setFrom(fromMail);
            helper.setTo(user.getParentEmail());
            helper.setSubject("🔥 LockedIn Daily Focus Report");
            helper.setText(html , true);
            mailSender.send(message);
            System.out.println("✅ Report sent successfully to " + user.getParentEmail());

        }catch (Exception e){
            System.err.println("Error occurred while sending email: " + e.getMessage());
        }
    }




}
