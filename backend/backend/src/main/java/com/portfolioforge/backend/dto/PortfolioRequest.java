package com.portfolioforge.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PortfolioRequest {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Name must be 2-100 characters")
    private String fullName;

    @NotBlank(message = "Professional title is required")
    @Size(max = 100, message = "Title must be under 100 characters")
    private String professionalTitle;

    private String location;

    @NotBlank(message = "Bio is required")
    @Size(min = 20, max = 400, message = "Bio must be 20-400 characters")
    private String bio;

    private String profilePhotoUrl;

    @NotBlank(message = "Slug is required")
    @Pattern(
            regexp = "^[a-z0-9-]{3,30}$",
            message = "Slug must be 3-30 characters, lowercase letters, numbers and hyphens only"
    )
    private String slug;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Pattern(
            regexp = "^\\+?[0-9]{7,15}$",
            message = "Invalid phone number format"
    )
    private String phone;

    private String linkedinUrl;
    private String githubUrl;
    private String twitterUrl;

    @NotBlank(message = "Colors are required")
    private String colorsJson;

    @NotBlank(message = "At least one skill is required")
    private String skillsJson;

    private String experienceJson;
    private String projectsJson;
    private String educationJson;
}