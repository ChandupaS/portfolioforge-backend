package com.portfolioforge.backend.dto;

import com.portfolioforge.backend.model.PortfolioStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioResponse {
    private String id;
    private String slug;
    private String fullDomain;
    private String fullName;
    private String professionalTitle;
    private PortfolioStatus status;
    private String message;
}