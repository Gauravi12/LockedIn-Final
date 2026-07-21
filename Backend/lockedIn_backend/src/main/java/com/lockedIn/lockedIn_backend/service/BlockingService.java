package com.lockedIn.lockedIn_backend.service;

import com.lockedIn.lockedIn_backend.dto.ContentRequest;
import com.lockedIn.lockedIn_backend.dto.ContentResponse;
import com.lockedIn.lockedIn_backend.entity.BlockedAttempt;
import com.lockedIn.lockedIn_backend.entity.User;
import com.lockedIn.lockedIn_backend.repository.BlockedAttemptRepo;
import com.lockedIn.lockedIn_backend.repository.UserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class BlockingService {

    private final BlockedAttemptRepo blockedAttemptRepo;
    private final UserRepo userRepo;

    public BlockingService(BlockedAttemptRepo blockedAttemptRepo , UserRepo userRepo){
        this.blockedAttemptRepo = blockedAttemptRepo;
        this.userRepo = userRepo;
    }

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void checkBlock(ContentResponse response , ContentRequest request){
        if(!response.isAllowed()){
            User user = getCurrentUser();

            BlockedAttempt blockedAttempt = new BlockedAttempt();

            blockedAttempt.setSource(request.getSource());
            blockedAttempt.setUser(user);
            blockedAttempt.setContent(request.getContent());
            blockedAttempt.setTimeStamp(java.time.LocalDateTime.now());
            blockedAttempt.setCategory(response.getClassification());

            blockedAttemptRepo.save(blockedAttempt);
        }
    }

}
