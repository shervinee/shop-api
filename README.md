shop-api (Spring Boot) — README

Short blurb

Opinionated Spring Boot 3 / Java 21 API starter for a small full‑stack app. Includes OpenAPI, sensible profiles, testing, and Docker.

Tech stack

Java 21 (Temurin), Spring Boot 3 (Web, Validation, Data JPA), Gradle Kotlin DSL

Database: PostgreSQL (dev via Docker)

API docs: springdoc‑openapi (Swagger UI)

Testing: JUnit 5, Testcontainers (Postgres)

Mapping: (optional) MapStruct + Lombok

Quickstart

Prereqs

Java 21 (Temurin), Gradle wrapper, Docker Desktop (optional for DB)

1) Boot a local Postgres

docker run --name shop-db -e POSTGRES_PASSWORD=postgres -e POSTGRES_USER=postgres -e POSTGRES_DB=shop -p 5432:5432 -d postgres:16

2) Run the API (local profile)

./gradlew bootRun --args='--spring.profiles.active=local'

Swagger UI: http://localhost:8080/swagger-ui/index.html

Configuration

Create src/main/resources/application-local.yml:

spring:
datasource:
url: jdbc:postgresql://localhost:5432/shop
username: postgres
password: postgres
jpa:
hibernate:
ddl-auto: update
properties:
hibernate.format_sql: true
server:
port: 8080

Environment variables that override the above (CI/Prod):

SPRING_DATASOURCE_URL, SPRING_DATASOURCE_USERNAME, SPRING_DATASOURCE_PASSWORD

SERVER_PORT

JWT_SECRET (if auth is added)

Common Gradle tasks

./gradlew clean build       # build + run unit tests
./gradlew test              # run tests only
./gradlew bootRun           # start app (uses default profile)

Project layout

com.example.shop
├─ api       // controllers (DTO in/out), exception handlers
├─ domain    // entities + repositories
├─ service   // business logic
├─ config    // configuration (OpenAPI, CORS, etc.)
└─ util

API design notes

Prefer DTOs at the controller boundary

Validate request bodies with jakarta.validation

Pagination: Spring Page<T> with page and size query params

Error shape: RFC7807 Problem Details (optional)

Testing

./gradlew test

Unit tests for services

Slice tests for controllers (@WebMvcTest)

(Optional) @Testcontainers for repository integration

Code style

Apply Spotless (Google Java Format) — sample build.gradle.kts snippet:

plugins { id("com.diffplug.spotless") version "6.25.0" }
spotless { java { googleJavaFormat() } kotlinGradle { ktlint() } }

Run: ./gradlew spotlessApply

Docker

Build & run:
docker build -t shop-api .
docker run -p 8080:8080 --env-file .env shop-api