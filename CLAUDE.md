# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

A learning assistant and Q&A platform for university students, integrating LLM + RAG for intelligent Q&A, course management, knowledge point analysis, and personalized learning recommendations. Three role-based interfaces: student (orange), teacher (green), admin (purple).

## Build & Run Commands

### Backend (Spring Boot multi-module Maven project)
```bash
cd 后端/backend
mvn clean install -DskipTests          # build all modules
cd la-application
mvn spring-boot:run                     # run dev server (port 8080)
java -jar target/la-application-1.0.0.jar --spring.profiles.active=dev  # run from jar
```

### Frontend (Vue 3 + Vite)
```bash
cd 前端
npm install
npm run dev        # dev server on port 3000
npm run build      # production build to dist/
npm run preview    # preview production build
```

### Database
```bash
mysql -u root -p < 数据库/init.sql    # initialize MySQL (learning_assistant db)
```

Prerequisites: JDK 17+, Node.js 18+, MySQL 8.0+, Redis 7.0+

## Architecture

### Backend - Maven multi-module (package: `com.learning`)
Module dependency order: `la-common` → `la-domain` → `la-infrastructure` → `la-service` → `la-application`

- **la-common**: Constants, enums, custom exceptions, unified `Result<T>` response wrapper, utility classes
- **la-domain**: Entity classes (9), DTOs (10), VOs (5) — data models shared across modules
- **la-infrastructure**: Spring Security config + JWT auth, MyBatis-Plus mappers, infrastructure configs
- **la-service**: Controllers (6) and service classes (5) — all business logic lives here
- **la-application**: Spring Boot entry point (`LearningAssistantApplication.java`), application.yml profiles (dev/prod)

API base path: `/api/`. Swagger docs at `/swagger-ui.html`.

### Frontend
- **src/api/**: Axios-based API modules (`auth.js`, `user.js`, `course.js`, `qa.js`, `admin.js`)
- **src/views/**: Three role-based route groups under `/student`, `/teacher`, `/admin`, plus shared `login/` and `error/`
- **src/stores/**: Pinia stores (`user.js`, `app.js`)
- **src/router/**: Route guards check JWT token and role-based access per route meta
- **src/mock/**: Frontend mock data (`data.js`)
- **src/assets/styles/global.scss**: Theme system with CSS variables — three color schemes (orange/green/purple)

Vite dev proxy: `/api` → `http://localhost:8080` (strips `/api` prefix via rewrite).

### Key Patterns
- Backend uses unified `Result<T>` wrapper for all API responses
- Global exception handler provides consistent error format
- JWT stateless auth with BCrypt password hashing
- RBAC: three roles (student/teacher/admin) enforced at route and API level
- LLM and face recognition are **mock implementations** — not connected to real APIs
- Frontend uses `@` alias mapped to `src/`
- Each role has its own Layout component with themed sidebar (gradient backgrounds)

### Config (application-dev.yml defaults)
- MySQL: `localhost:3306/learning_assistant`, user `root`, password `root123`
- Redis: `localhost:6379`
- JWT secret: `learning-assistant-jwt-secret-key-2024` (change for production)

## Demo Accounts

| Role | Username | Password |
|------|----------|----------|
| Student | 202011604100 | student123 |
| Teacher | 2020001 | teacher123 |
| Admin | admin | admin123 |
