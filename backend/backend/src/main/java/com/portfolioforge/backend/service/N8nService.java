package com.portfolioforge.backend.service;

import com.portfolioforge.backend.model.Portfolio;
import com.portfolioforge.backend.model.PortfolioStatus;
import com.portfolioforge.backend.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class N8nService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Value("${n8n.webhook.url}")
    private String n8nWebhookUrl;

    private final WebClient webClient = WebClient.builder().build();

    public boolean triggerDeployment(String portfolioId) {

        // 1. Find portfolio
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        // 2. Build payload to send to n8n
        Map<String, Object> payload = new HashMap<>();
        payload.put("portfolioId",      portfolio.getId());
        payload.put("slug",             portfolio.getSlug());
        payload.put("fullDomain",       portfolio.getFullDomain());
        payload.put("fullName",         portfolio.getFullName());
        payload.put("professionalTitle",portfolio.getProfessionalTitle());
        payload.put("location",         portfolio.getLocation());
        payload.put("bio",              portfolio.getBio());
        payload.put("profilePhotoUrl",  portfolio.getProfilePhotoUrl());
        payload.put("linkedinUrl",      portfolio.getLinkedinUrl());
        payload.put("githubUrl",        portfolio.getGithubUrl());
        payload.put("twitterUrl",       portfolio.getTwitterUrl());
        payload.put("colorsJson",       portfolio.getColorsJson());
        payload.put("skillsJson",       portfolio.getSkillsJson());
        payload.put("experienceJson",   portfolio.getExperienceJson());
        payload.put("projectsJson",     portfolio.getProjectsJson());
        payload.put("educationJson",    portfolio.getEducationJson());
        payload.put("email",            portfolio.getUser().getEmail());
        payload.put("phone",            portfolio.getUser().getPhone());

        // 3. Update status to GENERATING
        portfolio.setStatus(PortfolioStatus.GENERATING);
        portfolioRepository.save(portfolio);

        // 4. Call n8n webhook
        try {
            webClient.post()
                    .uri(n8nWebhookUrl)
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe(
                            response -> System.out.println("n8n triggered: " + response),
                            error -> {
                                System.err.println("n8n error: " + error.getMessage());
                                // Set status back to DRAFT if n8n fails
                                portfolio.setStatus(PortfolioStatus.FAILED);
                                portfolioRepository.save(portfolio);
                            }
                    );
            return true;

        } catch (Exception e) {
            portfolio.setStatus(PortfolioStatus.FAILED);
            portfolioRepository.save(portfolio);
            return false;
        }
    }

    // Called BY n8n when deployment is complete
    public void markAsLive(String portfolioId, String vercelProjectId,
                           String vercelDeploymentId) {
        portfolioRepository.findById(portfolioId).ifPresent(portfolio -> {
            portfolio.setStatus(PortfolioStatus.LIVE);
            portfolio.setVercelProjectId(vercelProjectId);
            portfolio.setVercelDeploymentId(vercelDeploymentId);
            portfolioRepository.save(portfolio);
        });
    }
}