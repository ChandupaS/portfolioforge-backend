package com.portfolioforge.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "google_id", unique = true, nullable = false)
    private String googleId;

    @Column(nullable = false)
    private String email;

    private String name;
    private String picture;
    private String phone;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Portfolio portfolio;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}

