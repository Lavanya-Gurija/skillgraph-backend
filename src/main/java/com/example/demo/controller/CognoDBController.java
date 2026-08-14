package com.example.demo.controller;

import com.example.demo.service.CognoDBService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CognoDBController {

    private final CognoDBService cognoDBService;

    public CognoDBController(CognoDBService cognoDBService) {
        this.cognoDBService = cognoDBService;
    }

    @GetMapping("/api/test")
    public String testConnection() {
        return cognoDBService.testConnection();
    }
}