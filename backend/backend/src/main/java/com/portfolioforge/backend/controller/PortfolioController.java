package com.portfolioforge.backend.controller;

import com.portfolioforge.backend.dto.PortfolioRequest;
import com.portfolioforge.backend.dto.PortfolioResponse;
import com.portfolioforge.backend.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/portfolio")
@CrossOrigin(origins = "*")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    // POST /api/portfolio/save
    @PostMapping("/save")
    public ResponseEntity<PortfolioResponse> savePortfolio(
            @RequestBody PortfolioRequest request) {
        PortfolioResponse response = portfolioService.savePortfolio(request);
        return ResponseEntity.ok(response);
    }

    // GET /api/portfolio/status/{id}
    @GetMapping("/status/{id}")
    public ResponseEntity<PortfolioResponse> getStatus(
            @PathVariable String id) {
        PortfolioResponse response = portfolioService.getStatus(id);
        return ResponseEntity.ok(response);
    }
}