package com.portfolioforge.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "portfolios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // --- Slug & Domain ---
    @Column(unique = true, nullable = false)
    private String slug;

    @Column(name = "full_domain")
    private String fullDomain;

    // --- Personal Details ---
    @Column(name = "full_name")
    private String fullName;

    @Column(name = "professional_title")
    private String professionalTitle;

    private String location;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "profile_photo_url")
    private String profilePhotoUrl;

    // --- Social Links ---
    @Column(name = "linkedin_url")
    private String linkedinUrl;

    @Column(name = "github_url")
    private String githubUrl;

    @Column(name = "twitter_url")
    private String twitterUrl;

    // --- Theme ---
    @Column(name = "colors_json", columnDefinition = "TEXT")
    private String colorsJson;

    // --- Content ---
    @Column(name = "skills_json", columnDefinition = "TEXT")
    private String skillsJson;

    @Column(name = "experience_json", columnDefinition = "TEXT")
    private String experienceJson;

    @Column(name = "projects_json", columnDefinition = "TEXT")
    private String projectsJson;

    @Column(name = "education_json", columnDefinition = "TEXT")
    private String educationJson;

    // --- Vercel ---
    @Column(name = "vercel_project_id")
    private String vercelProjectId;

    @Column(name = "vercel_deployment_id")
    private String vercelDeploymentId;

    // --- Status ---
    @Enumerated(EnumType.STRING)
    private PortfolioStatus status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = PortfolioStatus.DRAFT;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}