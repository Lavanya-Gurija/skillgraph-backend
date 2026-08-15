# SkillGraph Backend

A Spring Boot application backed by **CognoDB**, a managed graph database, for exploring relationships between developers, skills, learning paths, and projects.

SkillGraph demonstrates how graph-based data modeling and Cypher queries can be used to solve relationship-oriented problems such as skill discovery, prerequisite learning paths, and related-skill recommendations.

---

## Live Demo

* **Frontend:** https://skill-graph-ui.onrender.com/
* **Backend API:** https://skillgraph-backend-0fsg.onrender.com/

---

## Use Case

SkillGraph helps users explore a developer's skills, discover prerequisite learning paths, and find related skills based on shared developer knowledge.

The application demonstrates how graph relationships can be used to answer connected-data questions naturally.

The system supports the following use cases:

* Find all skills belonging to a developer.
* Find prerequisite skills across multiple levels.
* Find related skills through developers who share common skills.
* Explore the relationship between developers and projects.
* Explore technologies used by projects.
* Load predefined graph data for testing and demonstration.

---

## Why a Graph Database?

The main focus of SkillGraph is the relationships between developers, skills, prerequisites, and projects.

A graph database is a good fit because the application needs to traverse these relationships rather than simply retrieve independent records.

For example:

* Find all skills belonging to a developer.
* Find prerequisite skills across multiple levels.
* Find related skills through developers who share common skills.
* Explore technologies used by projects.

The learning-path query performs a multi-hop traversal through `PREREQUISITE_OF` relationships.

The related-skills query traverses from a skill to developers and then back to other skills through `HAS_SKILL` relationships.

In a relational database, these questions would require multiple joins and recursive queries. In a graph database, relationships are represented directly and can be traversed naturally using Cypher.

This makes the graph model well suited for exploring connected developer and skill data.

---

## Technology Stack

### Backend

* Java
* Spring Boot
* REST APIs
* Neo4j Java Driver
* Cypher
* Maven

### Database

* CognoDB
* OpenCypher
* Bolt protocol

### Frontend

* React.js
* Vite
* Axios

---

## Architecture

SkillGraph follows a layered backend architecture where the React.js frontend communicates with the Spring Boot backend through REST APIs.

The backend uses the official Neo4j Java Driver to communicate with CognoDB and execute Cypher queries.

```text
React.js Frontend
        │
        │ REST API
        ▼
Spring Boot Backend
        │
        ├── Controller Layer
        │
        ├── Service Layer
        │
        └── Neo4j Java Driver
                │
                │ Cypher Queries
                ▼
             CognoDB
          Graph Database
```

### Request Flow

1. The user interacts with the React.js frontend.
2. The frontend sends an HTTP request to the Spring Boot REST API.
3. The Controller receives the request.
4. The Service layer handles the application logic.
5. The Neo4j Java Driver executes the required Cypher query.
6. CognoDB traverses the graph relationships and returns the matching data.
7. The backend processes the result.
8. The REST API returns the response to the frontend.
9. The frontend displays the result to the user.

---

## Graph Data Model

The application contains three primary node types:

* `Developer`
* `Skill`
* `Project`

### Relationships

```text
Developer ──HAS_SKILL──────> Skill

Skill ──PREREQUISITE_OF────> Skill

Developer ──WORKED_ON──────> Project

Project ──USES─────────────> Skill

Skill ──RELATED_TO─────────> Skill
```

### Node Descriptions

| Node        | Description                                                       |
| ----------- | ----------------------------------------------------------------- |
| `Developer` | Represents a developer and their associated skills and projects.  |
| `Skill`     | Represents a technical skill or technology.                       |
| `Project`   | Represents a project associated with developers and technologies. |

### Relationship Descriptions

| Relationship      | Description                                               |
| ----------------- | --------------------------------------------------------- |
| `HAS_SKILL`       | Connects a developer to a skill they possess.             |
| `PREREQUISITE_OF` | Defines prerequisite relationships between skills.        |
| `WORKED_ON`       | Connects a developer to a project they worked on.         |
| `USES`            | Connects a project to the skills or technologies it uses. |
| `RELATED_TO`      | Represents a relationship between related skills.         |

---

## Seed Data

The application includes a seed service that creates sample developers, skills, projects, and relationships in CognoDB.

### Developers

* `Developer 1`
* `Developer 2`
* `Developer 3`

### Skills

* `Java`
* `Spring Boot`
* `Microservices`
* `REST API`
* `WebSocket`
* `MySQL`
* `OOP`
* `Git`
* `React`
* `JavaScript`

### Projects

* `EV Charging Platform`
* `Banking API`
* `Analytics Dashboard`

### Developer Skill Relationships

```text
Developer 1
 ├── Java
 ├── Spring Boot
 ├── REST API
 ├── WebSocket
 ├── MySQL
 └── Git

Developer 2
 ├── Java
 ├── Spring Boot
 ├── Microservices
 ├── REST API
 ├── MySQL
 └── Git

Developer 3
 ├── JavaScript
 ├── React
 ├── REST API
 └── Git
```

### Project Relationships

```text
Developer 1 ──WORKED_ON──> EV Charging Platform

Developer 2 ──WORKED_ON──> Banking API

Developer 3 ──WORKED_ON──> Analytics Dashboard
```

Projects are connected to their technologies using the `USES` relationship.

---

## Skill Prerequisite Relationships

The seed data defines the following learning path:

```text
Java
  │
  ▼
Spring Boot
  │
  ▼
Microservices
```

This allows the application to answer questions such as:

> What skills should be learned before Microservices?

The backend performs a multi-hop graph traversal to discover the prerequisite chain.

---

## API Documentation

The backend exposes REST APIs for loading seed data, retrieving developer skills, generating learning paths, discovering related skills, checking backend health, and testing CognoDB connectivity.

### API Summary

| Method | Endpoint                                | Purpose                          |
| ------ | --------------------------------------- | -------------------------------- |
| `POST` | `/api/seed`                             | Load sample graph data           |
| `GET`  | `/api/skills?developer={developerName}` | Retrieve a developer's skills    |
| `GET`  | `/api/learning-path?skill={skillName}`  | Find prerequisite learning paths |
| `GET`  | `/api/related-skills?skill={skillName}` | Find related skills              |
| `GET`  | `/api/health`                           | Check backend health             |
| `GET`  | `/api/test`                             | Test CognoDB connectivity        |

---

## 1. Load Seed Data

Loads the predefined developers, skills, projects, and graph relationships into CognoDB.

### Request

```http
POST /api/seed
```

### Example

```bash
curl -X POST http://localhost:8080/api/seed
```

### Response

```text
Seed data loaded successfully!
```

The seed operation uses `MERGE` statements so that repeated execution does not create duplicate nodes or relationships.

---

## 2. Get Developer Skills

Retrieves all skills associated with a developer.

### Request

```http
GET /api/skills?developer={developerName}
```

### Example

```http
GET /api/skills?developer=Developer%201
```

### Example Response

```json
[
  "Git",
  "Java",
  "MySQL",
  "REST API",
  "Spring Boot",
  "WebSocket"
]
```

The backend traverses:

```text
Developer ──HAS_SKILL──> Skill
```

The skills are returned alphabetically.

### Validation

If the developer parameter is missing or empty:

```json
{
  "success": false,
  "message": "Developer name is required."
}
```

---

## 3. Get Learning Path

Retrieves prerequisite skills for a given skill using a multi-hop graph traversal.

### Request

```http
GET /api/learning-path?skill={skillName}
```

### Example

```http
GET /api/learning-path?skill=Microservices
```

### Example Response

```json
[
  {
    "skill": "Spring Boot",
    "distance": 1
  },
  {
    "skill": "Java",
    "distance": 2
  }
]
```

The API traverses:

```text
Skill ──PREREQUISITE_OF──> Skill
```

The query supports up to five relationship hops:

```cypher
[:PREREQUISITE_OF*1..5]
```

The `distance` value represents the number of prerequisite relationship hops.

### Validation

If the skill parameter is missing or empty:

```json
{
  "success": false,
  "message": "Skill name is required."
}
```

---

## 4. Get Related Skills

Finds related skills by identifying developers who have the requested skill and then finding other skills those developers possess.

### Request

```http
GET /api/related-skills?skill={skillName}
```

### Example

```http
GET /api/related-skills?skill=Java
```

### Example Response

```json
[
  {
    "skill": "Git",
    "developers": 2
  },
  {
    "skill": "MySQL",
    "developers": 2
  },
  {
    "skill": "REST API",
    "developers": 2
  },
  {
    "skill": "Spring Boot",
    "developers": 2
  }
]
```

The graph traversal is:

```text
Skill
  │
  ▼
Developer
  │
  ▼
Related Skill
```

More specifically:

```text
Skill ──HAS_SKILL──< Developer >──HAS_SKILL──> Skill
```

The requested skill itself is excluded from the result.

The result is ordered by:

1. Number of developers sharing the skill, descending.
2. Skill name, alphabetically.

### Validation

If the skill parameter is missing or empty:

```json
{
  "success": false,
  "message": "Skill name is required."
}
```

---

## Cypher Queries

The application uses parameterized Cypher queries to perform graph traversals against CognoDB.

### Developer → Skills

```cypher
MATCH (d:Developer {name: $developerName})
      -[:HAS_SKILL]->(s:Skill)
RETURN s.name AS skill
ORDER BY s.name
```

### Learning Path

```cypher
MATCH path =
    (start:Skill {name: $skillName})
    -[:PREREQUISITE_OF*1..5]->(next:Skill)

RETURN next.name AS skill,
       min(length(path)) AS distance
ORDER BY distance
```

This is a multi-hop graph traversal because it can traverse between one and five `PREREQUISITE_OF` relationships.

### Related Skills

```cypher
MATCH (target:Skill {name: $skillName})
      <-[:HAS_SKILL]-
      (developer:Developer)
      -[:HAS_SKILL]->
      (related:Skill)

WHERE related <> target

RETURN related.name AS skill,
       count(DISTINCT developer) AS developers
ORDER BY developers DESC, skill
```

This query demonstrates a relationship pattern that would require multiple joins in a relational model.

All user-provided values are passed as Cypher parameters rather than concatenated directly into query strings.

---

## Project Structure

```text
skillgraph-backend/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/demo/
│   │   │       ├── config/
│   │   │       │   └── CognoDBConfig.java
│   │   │       │
│   │   │       ├── controller/
│   │   │       │   ├── SeedController.java
│   │   │       │   └── SkillController.java
│   │   │       │
│   │   │       ├── service/
│   │   │       │   ├── SeedService.java
│   │   │       │   └── SkillService.java
│   │   │       │
│   │   │       └── ...
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       └── ...
│
├── pom.xml
└── README.md
```

### Main Components

#### `CognoDBConfig`

Configures the CognoDB connection and creates the Neo4j Java Driver bean used by the application.

#### `SeedController`

Exposes the `/api/seed` endpoint for loading sample graph data.

#### `SkillController`

Exposes REST endpoints for:

* Developer skills
* Learning paths
* Related skills

#### `SkillService`

Contains the graph queries used to retrieve and analyze skill relationships.

#### `SeedService`

Creates the initial graph data using Cypher `MERGE` statements.

---

## CognoDB Setup

SkillGraph uses **CognoDB Cloud** as its managed graph database.

### 1. Create a CognoDB Account

Create an account through the CognoDB Cloud console:

https://console.cognodb.com/signup

### 2. Create a Free Instance

Create a free `c0` instance and select a region.

### 3. Save the Connection Details

CognoDB provides:

* Bolt URI
* Username
* Password

The password should be saved securely because it is provided when the database instance is created.

### 4. Configure Environment Variables

Set the following environment variables:

```bash
COGNODB_URI=<your-cognodb-uri>
COGNODB_USERNAME=cognodb
COGNODB_PASSWORD=<your-cognodb-password>
```

The CognoDB URI has the following general format:

```text
bolt+s://<instance-id>.databases.cognodb.cloud
```

---

## Configuration

The Spring Boot application reads CognoDB connection details from environment variables.

Example `application.properties`:

```properties
cognodb.uri=${COGNODB_URI}
cognodb.username=${COGNODB_USERNAME}
cognodb.password=${COGNODB_PASSWORD}
```

Real database credentials are never committed to the repository.

For local development, configure the environment variables in the operating system or development environment before starting the backend.

---

## Prerequisites

Make sure the following are installed before running the application:

* Java
* Maven
* Node.js and npm
* Git
* CognoDB database instance
* Configured CognoDB connection

---

## Running the Backend

### 1. Clone the Repository

```bash
git clone <repository-url>
```

### 2. Navigate to the Backend

```bash
cd skillgraph-backend
```

### 3. Configure Environment Variables

Set:

```bash
COGNODB_URI=<your-cognodb-uri>
COGNODB_USERNAME=cognodb
COGNODB_PASSWORD=<your-cognodb-password>
```

### 4. Build the Application

```bash
mvn clean install
```

### 5. Start the Application

```bash
mvn spring-boot:run
```

The Spring Boot backend will start on the configured server port.

---

## Loading Sample Data

After starting the backend, call:

```bash
curl -X POST http://localhost:8080/api/seed
```

Expected response:

```text
Seed data loaded successfully!
```

This creates the sample developers, skills, projects, and relationships in CognoDB.

---

## Running the Frontend

The project also includes a React.js frontend that consumes the Spring Boot REST APIs.

The frontend provides a user interface for exploring:

* Developer skills
* Learning paths
* Related skills

### Install Dependencies

Navigate to the frontend project:

```bash
cd <frontend-directory>
```

Install dependencies:

```bash
npm install
```

### Start the Development Server

```bash
npm run dev
```

The frontend communicates with the Spring Boot backend through REST APIs.

---

## Testing the APIs

### Health Check

```bash
curl http://localhost:8080/api/health
```

### CognoDB Connectivity Test

```bash
curl http://localhost:8080/api/test
```

### Load Seed Data

```bash
curl -X POST http://localhost:8080/api/seed
```

### Get Developer Skills

```bash
curl "http://localhost:8080/api/skills?developer=Developer%201"
```

### Get Learning Path

```bash
curl "http://localhost:8080/api/learning-path?skill=Microservices"
```

### Get Related Skills

```bash
curl "http://localhost:8080/api/related-skills?skill=Java"
```

---

## Error Handling

The backend includes centralized exception handling for invalid requests and database-related failures.

Invalid or missing request parameters are validated and returned with meaningful error messages.

If CognoDB is unavailable or a database operation fails, the backend handles the exception and returns a controlled API response instead of exposing raw database exceptions.

This allows the frontend to display an appropriate error state to the user.

---

## Testing

The application was tested to verify the main backend and frontend functionality, including:

* CognoDB connectivity
* Seed data loading
* Developer skill retrieval
* Learning-path traversal
* Related-skill discovery
* Graph relationship queries
* REST API request validation
* Frontend-to-backend communication
* API response validation
* Error handling for invalid requests

The APIs were tested with both valid and invalid request parameters to verify successful responses and validation behavior.

---

## Key Graph Capabilities

### 1. Developer Skill Discovery

The application can identify all skills associated with a developer through:

```text
Developer ──HAS_SKILL──> Skill
```

### 2. Multi-Hop Learning Paths

The application can traverse multiple prerequisite relationships:

```text
Java
  ↓
Spring Boot
  ↓
Microservices
```

This allows users to discover prerequisite skills for a target technology.

### 3. Related Skill Discovery

The application can identify related skills by using shared developer knowledge:

```text
Java
 ↓
Developer 1 / Developer 2
 ↓
Git / MySQL / REST API / Spring Boot
```

The number of developers sharing each skill is calculated and used to rank the results.

### 4. Project Technology Relationships

Projects are connected to the technologies they use:

```text
EV Charging Platform
 ├── Java
 ├── Spring Boot
 ├── WebSocket
 └── MySQL
```

This allows the graph to represent both people and technology relationships.

---

## Design Decisions

### Why Cypher?

Cypher provides a natural way to express graph patterns and relationship traversal.

Instead of performing multiple joins, the application can directly describe the relationships it wants to traverse.

### Why the Neo4j Java Driver?

CognoDB supports the official Neo4j Java Driver through the Bolt protocol. The driver provides a straightforward way for the Spring Boot application to establish a connection to the graph database and execute parameterized Cypher queries.

### Why `MERGE` for Seed Data?

The seed service uses `MERGE` so that running the seed endpoint multiple times does not create duplicate nodes or relationships.

### Why Parameterized Queries?

Developer names and skill names are passed as Cypher parameters rather than being directly concatenated into query strings.

This keeps the queries clean and avoids constructing queries from raw user input.

---

## Future Enhancements

Possible future improvements include:

* Authentication and authorization
* Developer profile management
* Skill proficiency levels
* Advanced learning-path recommendations
* Developer recommendations based on skill similarity
* Project recommendations based on developer skills
* Interactive graph visualization
* Pagination and filtering
* More comprehensive automated tests
* Production monitoring

---

## Conclusion

SkillGraph demonstrates how **CognoDB and graph-based data modeling** can be used to solve relationship-heavy application problems.

By representing developers, skills, projects, and their relationships directly in a graph, the application can naturally perform connected-data queries such as:

* Developer skill discovery
* Multi-level prerequisite learning paths
* Related-skill discovery
* Shared developer knowledge analysis
* Project technology exploration

The project combines **Spring Boot, REST APIs, Cypher, the Neo4j Java Driver, CognoDB, React.js, and Vite** to demonstrate a practical graph-based application architecture.
