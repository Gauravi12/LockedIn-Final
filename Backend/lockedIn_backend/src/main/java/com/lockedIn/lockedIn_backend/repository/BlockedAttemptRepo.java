package com.lockedIn.lockedIn_backend.repository;

import com.lockedIn.lockedIn_backend.entity.BlockedAttempt;
import com.lockedIn.lockedIn_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

public interface BlockedAttemptRepo extends JpaRepository<BlockedAttempt , Long> {
    @Query("""
    SELECT b.source
    FROM BlockedAttempt b
    WHERE b.user = :user
        AND b.timeStamp >= :start
        AND b.timeStamp < :end
    GROUP BY b.source
    ORDER BY COUNT(b) DESC
""")
    List<String> findTopDistractions(
            User user,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );
}
