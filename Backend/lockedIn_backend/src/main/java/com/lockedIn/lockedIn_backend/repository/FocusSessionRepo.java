package com.lockedIn.lockedIn_backend.repository;

import com.lockedIn.lockedIn_backend.entity.FocusSession;
import com.lockedIn.lockedIn_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FocusSessionRepo extends JpaRepository<FocusSession, Long> {
    Optional<FocusSession> findByUserAndState(User user , String state);
    List<FocusSession> findByUserAndSessionStartBetween(User user , LocalDateTime start , LocalDateTime end);
}
