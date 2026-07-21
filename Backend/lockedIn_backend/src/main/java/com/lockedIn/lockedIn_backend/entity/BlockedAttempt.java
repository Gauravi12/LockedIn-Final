package com.lockedIn.lockedIn_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.text.html.HTMLDocument;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BlockedAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String content;
    private String source;
    private String category;
    private LocalDateTime timeStamp;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

}
