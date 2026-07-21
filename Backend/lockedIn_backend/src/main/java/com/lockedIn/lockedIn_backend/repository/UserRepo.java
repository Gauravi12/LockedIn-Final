package com.lockedIn.lockedIn_backend.repository;

import com.lockedIn.lockedIn_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User , Long> {
    Optional<User> findByEmail(String email);
}
