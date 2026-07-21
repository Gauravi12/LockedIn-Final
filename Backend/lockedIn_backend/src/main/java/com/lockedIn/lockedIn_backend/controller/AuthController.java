package com.lockedIn.lockedIn_backend.controller;

import com.lockedIn.lockedIn_backend.dto.AuthResponse;
import com.lockedIn.lockedIn_backend.dto.LoginRequest;
import com.lockedIn.lockedIn_backend.dto.RegisterRequest;
import com.lockedIn.lockedIn_backend.service.AuthService;
import com.lockedIn.lockedIn_backend.service.SessionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final SessionService sessionService;

    public AuthController(AuthService authService , SessionService sessionService){
        this.authService = authService;
        this.sessionService = sessionService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request){
        return authService.login(request);
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request){
        return authService.register(request);
    }

    @PostMapping("/logOut")
    public void logOut(){
        sessionService.endSession();
        authService.logOut();
    }
}
