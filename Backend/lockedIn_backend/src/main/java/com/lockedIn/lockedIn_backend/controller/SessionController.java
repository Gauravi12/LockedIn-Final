package com.lockedIn.lockedIn_backend.controller;

import com.lockedIn.lockedIn_backend.entity.FocusSession;
import com.lockedIn.lockedIn_backend.service.SessionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/session")
@CrossOrigin(origins = "*")
public class SessionController {
    private final SessionService sessionService;

    public SessionController(SessionService sessionService){
        this.sessionService = sessionService;
    }

    @PostMapping("/start")
    public String startSession(){
        sessionService.startSession();
        return "session started";
    }

    @PostMapping("/pause")
    public String pauseSession(){
        sessionService.pauseSession();
        return "session paused";
    }

    @PostMapping("/resume")
    public String resumeSession(){
        sessionService.resumeSession();
        return "session resumed";
    }

    @PostMapping("/end")
    public String endSession(){
        sessionService.endSession();
        return "session ended";
    }

    @GetMapping("/current")
    public FocusSession getCurrentSession(){
        return sessionService.getCurrentSession();
    }

    @PostMapping("/add/distraction")
    public String addDistraction(@RequestBody long idleTime){
        System.out.println("========== ADD DISTRACTION HIT ==========");
        sessionService.addDistraction(idleTime);
        return "distraction added";
    }

    @GetMapping("/status")
    public String getStatus(){
        FocusSession session = getCurrentSession();
        if(session == null){
            return "ENDED";
        }
        return session.getState();
    }

}
