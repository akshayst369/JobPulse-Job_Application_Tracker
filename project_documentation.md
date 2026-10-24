# 📋 JobPulse — Complete Project Documentation & Technical Specification

> **Project Name:** JobPulse  
> **Author:** Akshay  
> **Type:** Full-Stack Job Application & Interview Round Tracker  
> **Backend:** Java 17, Spring Boot, Spring Data JPA, Hibernate, MySQL  
> **Frontend:** Vanilla HTML5, CSS3, JavaScript (Fetch API)  
> **Architecture:** 3-Tier Layered Architecture (Controller -> Service -> Repository -> Database)  

---

## 📑 Table of Contents
1. [Executive Summary & Purpose](#1-executive-summary--purpose)
2. [High-Level Architecture](#2-high-level-architecture)
3. [Project Directory & File Structure](#3-project-directory--file-structure)
4. [Data Model & Database Schema](#4-data-model--database-schema)
5. [Core Business Logic & Workflows](#5-core-business-logic--workflows)
6. [Complete REST API Reference](#6-complete-rest-api-reference)
7. [Frontend Architecture & UI Modules](#7-frontend-architecture--ui-modules)
8. [Cross-Cutting Concerns (CORS & Exception Handling)](#8-cross-cutting-concerns)
9. [Configuration & Environment](#9-configuration--environment)
10. [Step-by-Step Setup & Execution](#10-step-by-step-setup--execution)
11. [Key Strengths & Interview Talking Points](#11-key-strengths--interview-talking-points)

---

## 1. Executive Summary & Purpose

### What is JobPulse?
**JobPulse** is a full-stack web application engineered to help candidates, students, and job seekers systematically track and manage their recruitment pipelines and interview schedules.

### What Problem Does it Solve?
During placement seasons and active job hunts, candidates apply to dozens or hundreds of positions across LinkedIn, company career portals, and referrals. Organizing rounds (Aptitude, Coding, Technical, HR), deadlines, notes, and statuses across spreadsheets quickly becomes messy and error-prone.

JobPulse solves this by offering:
- **Centralized Job Tracking:** Company names, roles, applied dates, links, priority levels, and custom notes.
- **Dynamic Interview Pipelines:** Add sequential rounds to individual jobs.
- **Automated Status Calculation:** When round outcomes are updated (e.g. failed a round, or cleared all rounds), the overarching job status automatically recalculates without manual input.
- **Soft Deletion:** Jobs are never hard-deleted from MySQL, keeping audit history safe.
- **Zero-Dependency Lightweight Frontend:** Pure HTML/CSS/JS served straight from Spring Boot.

---

## 2. High-Level Architecture

The project follows a standard **Layered Architecture (N-Tier)**:

```text
┌────────────────────────────────────────────────────────┐
│             Presentation Layer (Frontend)              │
│   - index.html (Application creation form)             │
│   - dashboard.html (Analytics, filters, table, rounds) │
└───────────────────────────┬────────────────────────────┘
                            │ HTTP JSON API / CORS
┌───────────────────────────▼────────────────────────────┐
│              Controller Layer (REST APIs)              │
│   - JobController (/api/jobs)                          │
│   - RoundController (/api/jobs/{jobId}/rounds)         │
│   - GlobalExceptionHandler (@RestControllerAdvice)     │
└───────────────────────────┬────────────────────────────┘
                            │ Service Method Calls
┌───────────────────────────▼────────────────────────────┐
│                 Service Layer (Business)               │
│   - JobService (Job CRUD, soft delete, pagination)     │
│   - RoundService (Rounds logic, auto status update)    │
└───────────────────────────┬────────────────────────────┘
                            │ Spring Data JPA
┌───────────────────────────▼────────────────────────────┐
│                Repository Layer (Persistence)          │
│   - JobRepository (Derived queries, soft-delete filter)│
│   - RoundRepository (Queries by Job ID)                │
└───────────────────────────┬────────────────────────────┘
                            │ JDBC / SQL
┌───────────────────────────▼────────────────────────────┐
│                   MySQL Database                       │
│   - job_applications table                             │
│   - rounds table                                       │
└────────────────────────────────────────────────────────┘
```

---

## 3. Project Directory & File Structure

```text
Jobpulse_Project/
├── pom.xml                                    # Maven dependencies & build plugins
├── mvnw / mvnw.cmd                            # Maven wrapper scripts
├── README.md                                  # Quick-start documentation with screenshots
├── PROJECT_DOCUMENTATION.md                   # Complete architectural and technical reference
│
└── src/
    ├── main/
    │   ├── java/com/akshay/jobpulse/
    │   │   ├── JobpulseApplication.java       # Spring Boot main class (@SpringBootApplication)
    │   │   ├── CorsConfig.java                # Global CORS WebMvc configuration
    │   │   │
    │   │   ├── controller/                    # HTTP REST Endpoints
    │   │   │   ├── JobController.java         # Job CRUD & pagination endpoints
    │   │   │   └── RoundController.java       # Round management & status endpoints
    │   │   │
    │   │   ├── dto/                           # Data Transfer Objects
    │   │   │   └── PaginatedResponse.java     # Generic wrapper for paginated results
    │   │   │
    │   │   ├── exception/                     # Global Error Handling
    │   │   │   ├── GlobalExceptionHandler.java# Intercepts errors, returns structured JSON
    │   │   │   └── ResourceNotFoundException.java # Custom 404 runtime exception
    │   │   │
    │   │   ├── model/                         # JPA Entities and Enums
    │   │   │   ├── JobApplication.java        # Core job application entity
    │   │   │   ├── JobStatus.java             # Enum: ACTIVE, OFFER, REJECTED
    │   │   │   ├── Priority.java              # Enum: HIGH, MEDIUM, LOW
    │   │   │   ├── Round.java                 # Interview round entity
    │   │   │   └── RoundStatus.java           # Enum: PENDING, CLEARED, FAILED
    │   │   │
    │   │   ├── repository/                    # Spring Data JPA Data Access
    │   │   │   ├── JobRepository.java         # Queries on job_applications
    │   │   │   └── RoundRepository.java       # Queries on rounds
    │   │   │
    │   │   └── service/                       # Business Logic
    │   │       ├── JobService.java            # Job application operations & pagination
    │   │       └── RoundService.java          # Round pipeline logic & status propagation
    │   │
    │   └── resources/
    │       ├── application.properties         # Database connection, port, JPA settings
    │       └── static/                        # Frontend UI (Served automatically by Spring Boot)
    │           ├── index.html                 # "Add Application" entry page
    │           └── dashboard.html             # Analytics & dashboard pipeline view
    │
    └── test/
        └── java/com/akshay/jobpulse/
            └── JobpulseApplicationTests.java  # Spring Boot Context load test
```

---

## 4. Data Model & Database Schema

The database consists of two core tables linked by a **1-to-Many Relationship** (`job_applications` 1 ── * `rounds`).

```text
┌──────────────────────────────────────┐          ┌───────────────────────────────────┐
│           job_applications           │          │              rounds               │
├──────────────────────────────────────┤          ├───────────────────────────────────┤
│ id (PK, BigInt Auto-Inc)             │1        *│ id (PK, BigInt Auto-Inc)          │
│ company_name (VARCHAR, Not Null)     ├──────────┤ job_id (FK -> job_applications.id)│
│ role (VARCHAR, Not Null)             │          │ round_name (VARCHAR, Not Null)    │
│ location (VARCHAR)                   │          │ round_order (INT, Not Null)       │
│ job_link (VARCHAR)                   │          │ status (VARCHAR: Enum)            │
│ priority (VARCHAR: Enum, Not Null)   │          │ round_date (DATE)                 │
│ status (VARCHAR: Enum, Not Null)     │          │ notes (TEXT)                      │
│ applied_date (DATE, Not Null)        │          └───────────────────────────────────┘
│ is_deleted (BOOLEAN, Default false)  │
│ notes (TEXT)                         │
│ created_at (DATETIME)                │
│ updated_at (DATETIME)                │
└──────────────────────────────────────┘
```

---

## 5. Core Business Logic & Workflows

### A. Intelligent Status Synchronization
Job status is dynamically calculated from round outcomes:
- If **ANY** round is marked as `FAILED`:
  $$\text{Job Status} \rightarrow \mathbf{REJECTED}$$
- If **ALL** rounds are marked as `CLEARED`:
  $$\text{Job Status} \rightarrow \mathbf{OFFER}$$
- Otherwise (some cleared, some pending):
  $$\text{Job Status} \rightarrow \mathbf{ACTIVE}$$

### B. Soft-Delete Pattern
- `deleteJob(id)` sets `isDeleted = true` rather than executing SQL `DELETE`.
- Repository automatically queries only active items via `findByIsDeletedFalse()`.

### C. Server-Side Pagination
- Implements `Pageable` in `JobService` sorted by `createdAt` descending.
- Wrapped by generic DTO `PaginatedResponse<T>` returning page metadata (`currentPage`, `totalPages`, `totalElements`, `hasNext`, `hasPrevious`).

---

## 6. Complete REST API Reference

| Method | Endpoint | Description | Request Body | Response |
| :--- | :--- | :--- | :--- | :--- |
| **POST** | `/api/jobs` | Create new job application | `JobApplication` JSON | `201 Created` |
| **GET** | `/api/jobs` | Get all active jobs | None | `200 OK` |
| **GET** | `/api/jobs/paginated` | Get paginated jobs (`?page=0&size=10`) | None | `200 OK` |
| **GET** | `/api/jobs/{id}` | Get single job details | None | `200 OK` |
| **PUT** | `/api/jobs/{id}` | Update job details | `JobApplication` JSON | `200 OK` |
| **DELETE**| `/api/jobs/{id}` | Soft delete job | None | `200 OK` |
| **POST** | `/api/jobs/{jobId}/rounds` | Add round to job | `Round` JSON | `201 Created` |
| **GET** | `/api/jobs/{jobId}/rounds` | Get all rounds for job | None | `200 OK` |
| **PATCH**| `/api/jobs/{jobId}/rounds/{roundId}/status` | Update round status | `{"status": "CLEARED"}` | `200 OK` |
| **DELETE**| `/api/jobs/{jobId}/rounds/{roundId}` | Delete round | None | `200 OK` |

---

## 7. Frontend UI Modules
1. **`index.html`**: Clean submission form with validation and auto-redirect.
2. **`dashboard.html`**:
   - Stats summary counters (Total, Active, Offers, Rejected).
   - Live client filters (Search, Status filter, Priority filter).
   - Paginated data table with color badges.
   - Interactive slide-out Rounds drawer with round adding and real-time inline status toggling.

---

## 8. Setup & Execution

1. Create database:
   ```sql
   CREATE DATABASE jobpulse_db;
   ```
2. Run application:
   ```bash
   ./mvnw spring-boot:run
   ```
3. Open in browser: `http://localhost:8080/dashboard.html`
