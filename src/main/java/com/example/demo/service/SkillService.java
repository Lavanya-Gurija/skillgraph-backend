package com.example.demo.service;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SkillService {

    private final Driver driver;

    public SkillService(Driver driver) {
        this.driver = driver;
    }

    // 1. Developer -> Skills
    public List<String> getDeveloperSkills(String developerName) {

        String cypher = """
                MATCH (d:Developer {name: $developerName})
                      -[:HAS_SKILL]->(s:Skill)
                RETURN s.name AS skill
                ORDER BY s.name
                """;

        try (Session session = driver.session()) {

            return session.run(
                    cypher,
                    Map.of("developerName", developerName)
            ).list(record ->
                    record.get("skill").asString()
            );
        }
    }

    // 2. Multi-hop prerequisite learning path
    public List<Map<String, Object>> getLearningPath(String skillName) {

        String cypher = """
                MATCH path =
                    (start:Skill {name: $skillName})
                    -[:PREREQUISITE_OF*1..5]->(next:Skill)

                RETURN next.name AS skill,
                       min(length(path)) AS distance
                ORDER BY distance
                """;

        try (Session session = driver.session()) {

            return session.run(
                    cypher,
                    Map.of("skillName", skillName)
            ).list(record ->
                    Map.of(
                            "skill", record.get("skill").asString(),
                            "distance", record.get("distance").asInt()
                    )
            );
        }
    }

    // 3. Related skills through developers
    public List<Map<String, Object>> getRelatedSkills(String skillName) {

        String cypher = """
                MATCH (target:Skill {name: $skillName})
                      <-[:HAS_SKILL]-
                      (developer:Developer)
                      -[:HAS_SKILL]->
                      (related:Skill)

                WHERE related <> target

                RETURN related.name AS skill,
                       count(DISTINCT developer) AS developers
                ORDER BY developers DESC, skill
                """;

        try (Session session = driver.session()) {

            return session.run(
                    cypher,
                    Map.of("skillName", skillName)
            ).list(record ->
                    Map.of(
                            "skill", record.get("skill").asString(),
                            "developers", record.get("developers").asInt()
                    )
            );
        }
    }
}