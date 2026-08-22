# JobPulse Project Overview

JobPulse is a Spring Boot application designed as a **Job Application Tracker**. It allows users to track their job applications, interview rounds, and application statuses. 

## 🛠️ Technology Stack
- **Framework**: Spring Boot (using Web MVC)
- **Language**: Java 17
- **Build Tool**: Maven
- **Database / ORM**: MySQL Database & Spring Data JPA
- **Utilities**: Lombok (to reduce boilerplate code for getters/setters/constructors)
- **Frontend**: Static HTML files (`index.html`, `dashboard.html`) served directly by Spring Boot.

---

## 📂 Project Structure

The project follows a standard **N-Tier Architecture** (Model-View-Controller-Service-Repository), separating concerns into logical layers.

```text
src/main/
├── java/com/akshay/jobpulse/
│   ├── JobpulseApplication.java             # Main application entry point
│   │
│   ├── controller/                          # REST API Endpoints
│   │   ├── JobController.java               # Endpoints for managing job applications
│   │   └── RoundController.java             # Endpoints for managing interview rounds
│   │
│   ├── dto/                                 # Data Transfer Objects
│   │   └── PaginatedResponse.java           # Standardized response format for paginated data
│   │
│   ├── exception/                           # Error Handling
│   │   ├── GlobalExceptionHandler.java      # Catches and formats errors globally across APIs
│   │   └── ResourceNotFoundException.java   # Custom exception for missing records (e.g. 404)
│   │
│   ├── model/                               # Database Entities & Enums
│   │   ├── JobApplication.java              # Entity representing a single job application
│   │   ├── JobStatus.java                   # Enum (e.g., APPLIED, REJECTED, OFFER)
│   │   ├── Priority.java                    # Enum (e.g., HIGH, MEDIUM, LOW)
│   │   ├── Round.java                       # Entity representing an interview round for a job
│   │   └── RoundStatus.java                 # Enum for round statuses (e.g., SCHEDULED, CLEARED)
│   │
│   ├── repository/                          # Data Access Layer (Spring Data JPA)
│   │   ├── JobRepository.java               # Database operations for JobApplications
│   │   └── RoundRepository.java             # Database operations for Rounds
│   │
│   └── service/                             # Business Logic Layer
│       ├── JobService.java                  # Logic for creating, updating, and fetching jobs
│       └── RoundService.java                # Logic for managing interview rounds
│
└── resources/
    ├── application.properties               # Configuration (DB connection, server port, etc.)
    └── static/                              # Frontend static assets
        ├── dashboard.html                   # UI for the main application dashboard
        └── index.html                       # Landing page / entry UI
```

---

## 🧩 Component Breakdown

### 1. Models (Entities)
The domain is split into two primary entities:
- **`JobApplication`**: The core entity storing details about a job you applied for (Company, Role, etc.). It has a `JobStatus` and a `Priority`.
- **`Round`**: Represents a specific interview round associated with a `JobApplication` (e.g., HR Round, Technical). It has a `RoundStatus`.

### 2. Repositories
- **`JobRepository` & `RoundRepository`**: These extend Spring Data JPA interfaces (like `JpaRepository`). They handle all the SQL queries and database interactions automatically, allowing the application to save, update, delete, and find jobs/rounds in the MySQL database.

### 3. Services
- **`JobService` & `RoundService`**: This layer contains the core business rules. The controllers call these services, which then interact with the repositories. For example, `RoundService` would contain the logic to add a new round to an existing job application.

### 4. Controllers (APIs)
- **`JobController` & `RoundController`**: These classes expose the RESTful APIs (GET, POST, PUT, DELETE) that the frontend HTML files will consume to display data on the screen. 

### 5. Frontend & UI
- Instead of using a heavy frontend framework like React or Angular, this project currently serves vanilla HTML pages (`index.html` and `dashboard.html`) from the `src/main/resources/static` directory. These pages likely use JavaScript (Fetch API) to talk to the backend controllers.

### 6. Error Handling & Pagination
- The `GlobalExceptionHandler` intercepts errors (like asking for a Job ID that doesn't exist) and returns clean, structured HTTP error responses instead of crashing or exposing stack traces.
- `PaginatedResponse` ensures that when listing many job applications, the API can return them in chunks (pages) rather than all at once, improving performance.
