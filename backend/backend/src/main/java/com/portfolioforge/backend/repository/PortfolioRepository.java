package com.portfolioforge.backend.repository;

import com.portfolioforge.backend.model.Portfolio;
import com.portfolioforge.backend.model.PortfolioStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, String> {
    boolean existsBySlug(String slug);
    Optional<Portfolio> findBySlug(String slug);
    Optional<Portfolio> findByUserId(String userId);
    Optional<Portfolio> findByStatus(PortfolioStatus status);
}