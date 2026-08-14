package com.example.demo.service;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Service;

@Service
public class SeedService {

    private final Driver driver;

    public SeedService(Driver driver) {
        this.driver = driver;
    }

    public void seedData() {

        String cypher = """
                // Developers
                MERGE (d1:Developer {name: 'Developer 1'})
                MERGE (d2:Developer {name: 'Developer 2'})
                MERGE (d3:Developer {name: 'Developer 3'})

                // Skills
                MERGE (java:Skill {name: 'Java'})
                MERGE (spring:Skill {name: 'Spring Boot'})
                MERGE (microservices:Skill {name: 'Microservices'})
                MERGE (rest:Skill {name: 'REST API'})
                MERGE (websocket:Skill {name: 'WebSocket'})
                MERGE (mysql:Skill {name: 'MySQL'})
                MERGE (oop:Skill {name: 'OOP'})
                MERGE (git:Skill {name: 'Git'})
                MERGE (react:Skill {name: 'React'})
                MERGE (javascript:Skill {name: 'JavaScript'})

                // Projects
                MERGE (ev:Project {name: 'EV Charging Platform'})
                MERGE (banking:Project {name: 'Banking API'})
                MERGE (dashboard:Project {name: 'Analytics Dashboard'})

                // Developer 1 skills
                MERGE (d1)-[:HAS_SKILL]->(java)
                MERGE (d1)-[:HAS_SKILL]->(spring)
                MERGE (d1)-[:HAS_SKILL]->(rest)
                MERGE (d1)-[:HAS_SKILL]->(websocket)
                MERGE (d1)-[:HAS_SKILL]->(mysql)
                MERGE (d1)-[:HAS_SKILL]->(git)

                // Developer 2 skills
                MERGE (d2)-[:HAS_SKILL]->(java)
                MERGE (d2)-[:HAS_SKILL]->(spring)
                MERGE (d2)-[:HAS_SKILL]->(microservices)
                MERGE (d2)-[:HAS_SKILL]->(rest)
                MERGE (d2)-[:HAS_SKILL]->(mysql)
                MERGE (d2)-[:HAS_SKILL]->(git)

                // Developer 3 skills
                MERGE (d3)-[:HAS_SKILL]->(javascript)
                MERGE (d3)-[:HAS_SKILL]->(react)
                MERGE (d3)-[:HAS_SKILL]->(rest)
                MERGE (d3)-[:HAS_SKILL]->(git)

                // Skill relationships
                MERGE (java)-[:PREREQUISITE_OF]->(spring)
                MERGE (spring)-[:PREREQUISITE_OF]->(microservices)

                MERGE (oop)-[:RELATED_TO]->(java)
                MERGE (spring)-[:RELATED_TO]->(rest)
                MERGE (javascript)-[:RELATED_TO]->(react)

                // Projects
                MERGE (d1)-[:WORKED_ON]->(ev)
                MERGE (d2)-[:WORKED_ON]->(banking)
                MERGE (d3)-[:WORKED_ON]->(dashboard)

                MERGE (ev)-[:USES]->(java)
                MERGE (ev)-[:USES]->(spring)
                MERGE (ev)-[:USES]->(websocket)
                MERGE (ev)-[:USES]->(mysql)

                MERGE (banking)-[:USES]->(java)
                MERGE (banking)-[:USES]->(spring)
                MERGE (banking)-[:USES]->(microservices)
                MERGE (banking)-[:USES]->(mysql)

                MERGE (dashboard)-[:USES]->(javascript)
                MERGE (dashboard)-[:USES]->(react)
                MERGE (dashboard)-[:USES]->(rest)
                MERGE (dashboard)-[:USES]->(git)
                """;

        try (Session session = driver.session()) {
            session.run(cypher);
        }
    }
}