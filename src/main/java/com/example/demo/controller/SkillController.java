package com.example.demo.controller;

import com.example.demo.service.SkillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping("/api/skills")
    public ResponseEntity<?> getSkills(@RequestParam(required = false) String developer) {

        if (developer == null || developer.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "message", "Developer name is required."
                    ));
        }

        return ResponseEntity.ok(
                skillService.getDeveloperSkills(developer.trim())
        );
    }

    @GetMapping("/api/learning-path")
    public ResponseEntity<?> getLearningPath(
            @RequestParam(required = false) String skill) {

        if (skill == null || skill.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "message", "Skill name is required."
                    ));
        }

        return ResponseEntity.ok(
                skillService.getLearningPath(skill.trim())
        );
    }

    @GetMapping("/api/related-skills")
    public ResponseEntity<?> getRelatedSkills(
            @RequestParam(required = false) String skill) {

        if (skill == null || skill.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "message", "Skill name is required."
                    ));
        }

        return ResponseEntity.ok(
                skillService.getRelatedSkills(skill.trim())
        );
    }
}