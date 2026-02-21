package com.portfolioforge.backend.service;

import com.portfolioforge.backend.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SlugService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    // Reserved slugs that users cannot take
    private static final List<String> RESERVED_SLUGS = List.of(
            "admin", "api", "www", "mail", "portfolio",
            "portfolioforge", "login", "dashboard", "support",
            "help", "about", "contact", "blog", "app"
    );

    public boolean isValidFormat(String slug) {
        // Only lowercase letters, numbers, hyphens
        // Min 3 chars, max 30 chars
        return slug != null &&
                slug.matches("^[a-z0-9-]{3,30}$") &&
                !slug.startsWith("-") &&
                !slug.endsWith("-");
    }

    public boolean isAvailable(String slug) {
        if (!isValidFormat(slug)) return false;
        if (RESERVED_SLUGS.contains(slug.toLowerCase())) return false;
        return !portfolioRepository.existsBySlug(slug.toLowerCase());
    }

    public List<String> suggestAlternatives(String slug) {
        List<String> suggestions = new ArrayList<>();
        String base = slug.toLowerCase().replaceAll("[^a-z0-9-]", "");

        // Try adding numbers
        for (int i = 1; i <= 3; i++) {
            String suggestion = base + "-" + i;
            if (isAvailable(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        // Try adding "dev", "pro", "hq"
        for (String suffix : List.of("dev", "pro", "hq")) {
            String suggestion = base + "-" + suffix;
            if (isAvailable(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        return suggestions.stream().limit(3).toList();
    }

    public String buildFullDomain(String slug) {
        return slug + ".portfolioforge.com";
    }
}