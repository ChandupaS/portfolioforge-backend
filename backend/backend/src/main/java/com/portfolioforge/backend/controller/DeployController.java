package com.portfolioforge.backend.controller;

import com.portfolioforge.backend.dto.DeployRequest;
import com.portfolioforge.backend.service.N8nService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/deploy")
@CrossOrigin(origins = "*")
public class DeployController {

    @Autowired
    private N8nService n8nService;

    // POST /api/deploy/trigger
    // Called by frontend when user hits "Generate My Portfolio"
    @PostMapping("/trigger")
    public ResponseEntity<?> triggerDeploy(@RequestBody DeployRequest request) {
        boolean triggered = n8nService.triggerDeployment(request.getPortfolioId());

        if (triggered) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Portfolio generation started!",
                    "status", "GENERATING"
            ));
        } else {
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "message", "Failed to trigger deployment. Please try again.",
                    "status", "FAILED"
            ));
        }
    }

    // POST /api/deploy/complete
    // Called BY n8n when deployment finishes
    @PostMapping("/complete")
    public ResponseEntity<?> deployComplete(@RequestBody Map<String, String> payload) {
        String portfolioId      = payload.get("portfolioId");
        String vercelProjectId  = payload.get("vercelProjectId");
        String vercelDeployId   = payload.get("vercelDeploymentId");

        n8nService.markAsLive(portfolioId, vercelProjectId, vercelDeployId);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Portfolio marked as live!"
        ));
    }
}