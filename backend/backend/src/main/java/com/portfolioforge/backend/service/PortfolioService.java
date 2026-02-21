package com.portfolioforge.backend.service;

import com.portfolioforge.backend.dto.PortfolioRequest;
import com.portfolioforge.backend.dto.PortfolioResponse;
import com.portfolioforge.backend.model.Portfolio;
import com.portfolioforge.backend.model.PortfolioStatus;
import com.portfolioforge.backend.model.User;
import com.portfolioforge.backend.repository.PortfolioRepository;
import com.portfolioforge.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SlugService slugService;

    public PortfolioResponse savePortfolio(PortfolioRequest request) {

        // 1. Check slug is still available
        if (!slugService.isAvailable(request.getSlug())) {
            return PortfolioResponse.builder()
                    .message("Slug is no longer available. Please choose another.")
                    .status(PortfolioStatus.FAILED)
                    .build();
        }

        // 2. Find or create a temporary user
        // (will be replaced by real Google OAuth user later)
        User user = userRepository.findByEmail(request.getEmail())
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(request.getEmail())
                            .name(request.getFullName())
                            .phone(request.getPhone())
                            .googleId("temp-" + request.getEmail())
                            .build();
                    return userRepository.save(newUser);
                });

        // 3. Check if user already has a portfolio
        Optional<Portfolio> existing = portfolioRepository.findByUserId(user.getId());

        Portfolio portfolio;

        if (existing.isPresent()) {
            // Update existing portfolio
            portfolio = existing.get();
        } else {
            // Create new portfolio
            portfolio = new Portfolio();
            portfolio.setUser(user);
        }

        // 4. Map request to portfolio entity
        portfolio.setSlug(request.getSlug().toLowerCase());
        portfolio.setFullDomain(slugService.buildFullDomain(request.getSlug()));
        portfolio.setFullName(request.getFullName());
        portfolio.setProfessionalTitle(request.getProfessionalTitle());
        portfolio.setLocation(request.getLocation());
        portfolio.setBio(request.getBio());
        portfolio.setProfilePhotoUrl(request.getProfilePhotoUrl());
        portfolio.setLinkedinUrl(request.getLinkedinUrl());
        portfolio.setGithubUrl(request.getGithubUrl());
        portfolio.setTwitterUrl(request.getTwitterUrl());
        portfolio.setColorsJson(request.getColorsJson());
        portfolio.setSkillsJson(request.getSkillsJson());
        portfolio.setExperienceJson(request.getExperienceJson());
        portfolio.setProjectsJson(request.getProjectsJson());
        portfolio.setEducationJson(request.getEducationJson());
        portfolio.setStatus(PortfolioStatus.DRAFT);

        // 5. Save to Supabase
        Portfolio saved = portfolioRepository.save(portfolio);

        // 6. Return response
        return PortfolioResponse.builder()
                .id(saved.getId())
                .slug(saved.getSlug())
                .fullDomain(saved.getFullDomain())
                .fullName(saved.getFullName())
                .professionalTitle(saved.getProfessionalTitle())
                .status(saved.getStatus())
                .message("Portfolio saved successfully!")
                .build();
    }

    public PortfolioResponse getStatus(String portfolioId) {
        return portfolioRepository.findById(portfolioId)
                .map(p -> PortfolioResponse.builder()
                        .id(p.getId())
                        .slug(p.getSlug())
                        .fullDomain(p.getFullDomain())
                        .fullName(p.getFullName())
                        .status(p.getStatus())
                        .message("Status fetched successfully")
                        .build())
                .orElse(PortfolioResponse.builder()
                        .message("Portfolio not found")
                        .build());
    }
}