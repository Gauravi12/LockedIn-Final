package com.lockedIn.lockedIn_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private Boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

}
