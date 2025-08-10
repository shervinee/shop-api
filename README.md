# shop-api (Spring Boot) — README

Short blurb

> Opinionated Spring Boot 3 / Java 21 API starter for a small full-stack app. Includes OpenAPI, sensible profiles, testing, and Docker.

## Tech stack

- Java 21 (Temurin), Spring Boot 3 (Web, Validation, Data JPA), Gradle Kotlin DSL
- Database: PostgreSQL (dev via Docker)
- API docs: springdoc-openapi (Swagger UI)
- Testing: JUnit 5, Testcontainers (Postgres)
- Mapping: (optional) MapStruct + Lombok

## Quickstart

### Prereqs

- Java 21 (Temurin), Gradle wrapper, Docker Desktop (optional for DB)

### 1) Boot a local Postgres

```bash
docker run --name shop-db -e POSTGRES_PASSWORD=postgres -e POSTGRES_USER=postgres -e POSTGRES_DB=shop -p 5432:5432 -d postgres:16
```

### 2) Run the API (local profile)

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Configuration

Create `src/main/resources/application-local.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/shop
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: update
