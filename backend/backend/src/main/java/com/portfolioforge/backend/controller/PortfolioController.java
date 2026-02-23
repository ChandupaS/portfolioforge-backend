package com.portfolioforge.backend.controller;

import com.portfolioforge.backend.dto.DeployRequest;
import com.portfolioforge.backend.dto.PortfolioRequest;
import com.portfolioforge.backend.dto.PortfolioResponse;
import com.portfolioforge.backend.service.N8nService;
import com.portfolioforge.backend.service.PortfolioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/portfolio")
@CrossOrigin(origins = "*")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private N8nService n8nService;

    // POST /api/portfolio/save
    @PostMapping("/save")
    public ResponseEntity<PortfolioResponse> savePortfolio(
            @Valid @RequestBody PortfolioRequest request) {
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

    // PUT /api/portfolio/update
    @PutMapping("/update")
    public ResponseEntity<?> updatePortfolio(
            @Valid @RequestBody PortfolioRequest request) {
        PortfolioResponse response = portfolioService.updatePortfolio(request);
        return ResponseEntity.ok(response);
    }

    // POST /api/portfolio/redeploy
    @PostMapping("/redeploy")
    public ResponseEntity<?> redeploy(
            @RequestBody DeployRequest request) {
        boolean triggered = n8nService.triggerDeployment(
                request.getPortfolioId()
        );
        return triggered
                ? ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Redeployment started!",
                "status",  "GENERATING"
        ))
                : ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "Redeploy failed. Try again.",
                "status",  "FAILED"
        ));
    }
}
