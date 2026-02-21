package com.portfolioforge.backend.dto;

import lombok.Data;

@Data
public class PortfolioRequest {

    // Personal
    private String fullName;
    private String professionalTitle;
    private String location;
    private String bio;
    private String profilePhotoUrl;

    // Slug
    private String slug;

    // Social
    private String linkedinUrl;
    private String githubUrl;
    private String twitterUrl;

    // Contact
    private String email;
    private String phone;

    // Theme (JSON string from frontend)
    // e.g. ["#1a3a5c","#c8a96e","#f5f4f0","#1a1814"]
    private String colorsJson;

    // Content (JSON strings from frontend)
    private String skillsJson;
    private String experienceJson;
    private String projectsJson;
    private String educationJson;
}