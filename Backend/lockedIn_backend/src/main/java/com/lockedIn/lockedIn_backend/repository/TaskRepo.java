package com.lockedIn.lockedIn_backend.repository;

import com.lockedIn.lockedIn_backend.entity.Task;
import com.lockedIn.lockedIn_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskRepo extends JpaRepository<Task , Long> {
    List<Task> findByUserAndCompleted(User user , boolean completed);
    Optional<Task> findByIdAndUser(Long id , User user);
    List<Task> findByUser(User user);
    List<Task> findByUserAndCompletedAtBetween(
            User user,
            LocalDateTime start,
            LocalDateTime end
    );
}
