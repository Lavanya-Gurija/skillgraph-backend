# SkillGraph Backend

A Spring Boot backend that uses a graph database to model developers, skills, and skill prerequisites.

## Tech Stack

- Java
- Spring Boot
- CognoDB
- Neo4j Java Driver
- Maven
- REST APIs

## Graph Data Model

The application uses two node types:

- `Developer`
- `Skill`



                         ┌──────────────┐
                         │  Developer   │
                         └──────┬───────┘
                                │
                           HAS_SKILL
                                │
                                ▼
                         ┌──────────────┐
                         │    Skill     │
                         │     Java     │
                         └──────┬───────┘
                                │
                       PREREQUISITE_OF
                                │
                                ▼
                         ┌──────────────┐
                         │    Skill     │
                         │ Spring Boot  │
                         └──────┬───────┘
                                │
                       PREREQUISITE_OF
                                │
                                ▼
                         ┌──────────────┐
                         │    Skill     │
                         │ Microservices│
                         └──────────────┘

### Relationships

```text
Developer ──HAS_SKILL──> Skill

Skill ──PREREQUISITE_OF──> Skill