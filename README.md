# JobPulse 🚀

**Live:** https://jobpulse-jobapplicationtracker-production.up.railway.app

A Job Application Tracker built with Spring Boot and MySQL.

## Tech Stack
- Backend: Spring Boot 4.0.7 + Java 17
- Database: MySQL 8
- Frontend: HTML + Vanilla JS
- Deploy: Railway

## Features
- Add and manage job applications
- Dynamic round tracking per company
- Auto job status update when round status changes
- Dashboard with filters and analytics
- Soft delete with data preservation
- Delete individual rounds

## API Endpoints
- POST /api/jobs — create job
- GET /api/jobs — get all jobs
- GET /api/jobs/{id} — get job by id
- PUT /api/jobs/{id} — update job
- DELETE /api/jobs/{id} — soft delete
- POST /api/jobs/{jobId}/rounds — add round
- GET /api/jobs/{jobId}/rounds — get rounds
- PATCH /api/jobs/{jobId}/rounds/{id}/status — update round status

## Run Locally
1. Create MySQL database: `CREATE DATABASE jobpulse_db;`
2. Update `application.properties` with your MySQL credentials
3. Run `JobpulseApplication.java`
4. Open `http://localhost:8080`