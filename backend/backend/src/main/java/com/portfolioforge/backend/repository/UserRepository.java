package com.portfolioforge.backend.repository;

import com.portfolioforge.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByGoogleId(String googleId);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}