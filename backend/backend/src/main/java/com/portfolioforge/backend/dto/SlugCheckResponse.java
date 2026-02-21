package com.portfolioforge.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlugCheckResponse {
    private String slug;
    private boolean available;
    private String message;
    private String suggestedUrl;
}