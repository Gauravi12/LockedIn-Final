package com.lockedIn.lockedIn_backend.service;

import com.lockedIn.lockedIn_backend.entity.FocusSession;
import com.lockedIn.lockedIn_backend.entity.User;
import com.lockedIn.lockedIn_backend.repository.FocusSessionRepo;
import com.lockedIn.lockedIn_backend.repository.UserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SessionService {

    private final FocusSessionRepo focusSessionRepo;
    private final UserRepo userRepo;

    public SessionService(FocusSessionRepo focusSessionRepo , UserRepo userRepo){
        this.userRepo = userRepo;
        this.focusSessionRepo = focusSessionRepo;
    }

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void startSession(){
        User user = getCurrentUser();
        Optional<FocusSession> activeSession = focusSessionRepo.findByUserAndState(user , "ACTIVE");
        Optional<FocusSession> pausedSession = focusSessionRepo.findByUserAndState(user , "PAUSED");

        if(activeSession.isPresent()){
            throw new RuntimeException("Session already active");
        }else if (pausedSession.isPresent()){
            throw new RuntimeException("Resume or end the paused session first");
        }else{
            FocusSession focusSession = new FocusSession();
            focusSession.setState("ACTIVE");
            focusSession.setSessionStart(java.time.LocalDateTime.now());
            focusSession.setUser(user);
            focusSession.setDistractionCount(0);
            focusSession.setTotalDistractionSecond(0);
            focusSession.setTotalSleepSecond(0);
            System.out.println(
                    "NEW SESSION COUNT = "
                            + focusSession.getDistractionCount()
            );
            focusSessionRepo.save(focusSession);
        }
    }

    public void pauseSession(){
        FocusSession currentFocusSession = getCurrentSession();
        if(currentFocusSession == null){
            throw new RuntimeException("No active session");
        }
        if(currentFocusSession.getState().equals("ACTIVE")){
            currentFocusSession.setState("PAUSED");
            currentFocusSession.setPausedAt(java.time.LocalDateTime.now());
            focusSessionRepo.save(currentFocusSession);
        }else{
            throw new RuntimeException("Session is not Active");
        }
    }

    public void resumeSession(){
        FocusSession currentFocusSession = getCurrentSession();
        if(currentFocusSession == null){
            throw new RuntimeException("No paused session");
        }
        if(currentFocusSession.getState().equals("PAUSED")){
            currentFocusSession.setState("ACTIVE");
            currentFocusSession.setTotalSleepSecond(currentFocusSession.getTotalSleepSecond() + java.time.Duration.between(currentFocusSession.getPausedAt(), java.time.LocalDateTime.now()).getSeconds());
            currentFocusSession.setPausedAt(null);
            focusSessionRepo.save(currentFocusSession);
        }else{
            throw new RuntimeException("Session is not Paused");
        }
    }

    public void endSession(){
        FocusSession currentFocusSession = getCurrentSession();
        if(currentFocusSession == null){
            throw new RuntimeException("No active or paused session");
        }
        if(currentFocusSession.getState().equals("ACTIVE") || currentFocusSession.getState().equals("PAUSED")){
            if(currentFocusSession.getPausedAt() != null){
                currentFocusSession.setTotalSleepSecond(currentFocusSession.getTotalSleepSecond() + java.time.Duration.between(currentFocusSession.getPausedAt(), java.time.LocalDateTime.now()).getSeconds());
                currentFocusSession.setPausedAt(null);
            }
            currentFocusSession.setSessionEnd(java.time.LocalDateTime.now());
            currentFocusSession.setState("ENDED");
            focusSessionRepo.save(currentFocusSession);
        }
    }

    public FocusSession getCurrentSession(){
        User user = getCurrentUser();
        Optional<FocusSession> active = focusSessionRepo.findByUserAndState(user , "ACTIVE");
        if(active.isPresent()){
            return active.get();
        }
        Optional<FocusSession> paused = focusSessionRepo.findByUserAndState(user , "PAUSED");
        if(paused.isPresent()){
            return paused.get();
        }
        return null;
    }

    public void addDistraction(long idleTime){
        FocusSession session = getCurrentSession();
        if(session == null){
            throw new RuntimeException("No active session");
        }
        System.out.println("SESSION ID = " + session.getId());
        System.out.println("COUNT BEFORE = " + session.getDistractionCount());
        session.setTotalDistractionSecond(session.getTotalDistractionSecond() + idleTime);
        session.setDistractionCount(session.getDistractionCount() + 1);
        System.out.println("COUNT AFTER = " + session.getDistractionCount());
        focusSessionRepo.save(session);
    }


    public void update(FocusSession focusSession){
        focusSessionRepo.save(focusSession);
    }
}
