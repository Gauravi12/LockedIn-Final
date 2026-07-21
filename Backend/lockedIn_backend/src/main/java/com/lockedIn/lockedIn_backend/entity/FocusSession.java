package com.lockedIn.lockedIn_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FocusSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String state;
    private LocalDateTime sessionStart;
    private LocalDateTime sessionEnd;
    private long totalSleepSecond;
    private long totalDistractionSecond;
    private int distractionCount;
    private LocalDateTime pausedAt;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
}
