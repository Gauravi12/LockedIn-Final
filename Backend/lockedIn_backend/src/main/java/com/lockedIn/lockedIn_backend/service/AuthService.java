package com.lockedIn.lockedIn_backend.service;

import com.lockedIn.lockedIn_backend.dto.AuthResponse;
import com.lockedIn.lockedIn_backend.dto.LoginRequest;
import com.lockedIn.lockedIn_backend.dto.RegisterRequest;
import com.lockedIn.lockedIn_backend.entity.Role;
import com.lockedIn.lockedIn_backend.entity.User;
import com.lockedIn.lockedIn_backend.repository.UserRepo;
import com.lockedIn.lockedIn_backend.security.JWTService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(PasswordEncoder passwordEncoder, UserRepo userRepo, JWTService jwtService, AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepo.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        String token = jwtService.generateToken(user);
        user.setLastLogOutDate(null);
        userRepo.save(user);
        return new AuthResponse(token, user.getName());
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setParentEmail(request.getParentEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setCreatedAt(java.time.LocalDateTime.now());
        userRepo.save(user);
        AuthResponse response = login(new LoginRequest(user.getEmail(), request.getPassword()));
        return response;
    }

    public void logOut(){
        User user = getCurrentUser();
        user.setLastLogOutDate(LocalDateTime.now());
        userRepo.save(user);
    }
}
