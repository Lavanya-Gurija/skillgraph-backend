package com.example.demo.controller;

import com.example.demo.service.SeedService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SeedController {

    private final SeedService seedService;

    public SeedController(SeedService seedService) {
        this.seedService = seedService;
    }

    @PostMapping("/api/seed")
    public String seed() {

        seedService.seedData();

        return "Seed data loaded successfully!";
    }
}