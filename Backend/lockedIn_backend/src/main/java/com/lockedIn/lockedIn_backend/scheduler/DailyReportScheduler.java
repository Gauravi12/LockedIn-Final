package com.lockedIn.lockedIn_backend.scheduler;

import com.lockedIn.lockedIn_backend.entity.User;
import com.lockedIn.lockedIn_backend.repository.UserRepo;
import com.lockedIn.lockedIn_backend.service.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DailyReportScheduler {

    private final EmailService emailService;
    private final UserRepo userRepo;

    public DailyReportScheduler(EmailService emailService , UserRepo userRepo){
        this.userRepo = userRepo;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 35 0 * * *")
    public void sendDailyReport(){
        List<User> users = userRepo.findAll();
        for(User user : users){
            if (user.getLastLogOutDate() == null || user.getLastLogOutDate().toLocalDate().equals(LocalDate.now())) {
                emailService.sendReport(user);
            }
        }
    }
}

