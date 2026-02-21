package com.portfolioforge.backend.controller;

import com.portfolioforge.backend.dto.SlugCheckResponse;
import com.portfolioforge.backend.service.SlugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/slug")
@CrossOrigin(origins = "*")
public class SlugController {

    @Autowired
    private SlugService slugService;

    // GET /api/slug/check/john-smith
    @GetMapping("/check/{slug}")
    public ResponseEntity<?> checkSlug(@PathVariable String slug) {

        String cleanSlug = slug.toLowerCase().trim();

        // Check format first
        if (!slugService.isValidFormat(cleanSlug)) {
            return ResponseEntity.badRequest().body(
                    new SlugCheckResponse(
                            cleanSlug,
                            false,
                            "Slug must be 3-30 characters, lowercase letters, numbers and hyphens only",
                            null
                    )
            );
        }

        // Check availability
        boolean available = slugService.isAvailable(cleanSlug);

        if (available) {
            return ResponseEntity.ok(
                    new SlugCheckResponse(
                            cleanSlug,
                            true,
                            "✅ " + slugService.buildFullDomain(cleanSlug) + " is available!",
                            slugService.buildFullDomain(cleanSlug)
                    )
            );
        } else {
            // Suggest alternatives
            List<String> suggestions = slugService.suggestAlternatives(cleanSlug);
            return ResponseEntity.ok(
                    new SlugCheckResponse(
                            cleanSlug,
                            false,
                            "❌ This URL is already taken. Try one of the suggestions below.",
                            null
                    )
            );
        }
    }

    // GET /api/slug/suggestions/john-smith
    @GetMapping("/suggestions/{slug}")
    public ResponseEntity<?> getSuggestions(@PathVariable String slug) {
        List<String> suggestions = slugService.suggestAlternatives(slug);
        return ResponseEntity.ok(Map.of(
                "suggestions", suggestions,
                "domains", suggestions.stream()
                        .map(s -> slugService.buildFullDomain(s))
                        .toList()
        ));
    }
}