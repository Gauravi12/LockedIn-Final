package com.lockedIn.lockedIn_backend.repository;

import com.lockedIn.lockedIn_backend.entity.ContentClassification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContentClassificationRepo extends JpaRepository<ContentClassification , Long> {
    Optional<ContentClassification> findByContentAndSource(String content , String source);
}
