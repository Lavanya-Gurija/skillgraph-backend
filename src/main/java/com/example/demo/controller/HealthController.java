package com.example.demo.controller;

import com.example.demo.service.CognoDBService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    private final CognoDBService cognoDBService;

    public HealthController(CognoDBService cognoDBService) {
        this.cognoDBService = cognoDBService;
    }

    @GetMapping("/api/health")
    public ResponseEntity<Map<String, String>> health() {

        boolean connected = cognoDBService.isDatabaseConnected();

        if (connected) {
            return ResponseEntity.ok(
                    Map.of(
                            "status", "UP",
                            "database", "CONNECTED"
                    )
            );
        }

        return ResponseEntity.status(503).body(
                Map.of(
                        "status", "DOWN",
                        "database", "DISCONNECTED"
                )
        );
    }
}