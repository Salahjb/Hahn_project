# 🚀 Task Management System - Hahn Internship Challenge

A full-stack project management application built for the **Hahn Software Morocco Internship 2026** technical test. This application allows users to manage projects and tasks with a clean, responsive interface and a robust backend.

---

## 🏗️ Architecture & Technical Decisions

This project follows a modern **Microservice-ready Monolith** architecture, emphasizing clean code, scalability, and developer experience.

### 🔧 Backend (Spring Boot 3.4 + Java 17)

- **Layered Architecture:** Strict separation of concerns (Controller → Service → Repository).
- **Security:** Implemented **JWT (JSON Web Token)** authentication with Spring Security 6. Stateless session management ensures scalability.
- **Validation:** Comprehensive Input Validation using `jakarta.validation` (`@NotNull`, `@Size`, `@FutureOrPresent`) to ensure data integrity before it reaches the database.
- **Database:** **MySQL 8** used for persistence. JPA/Hibernate handles Object-Relational Mapping.
- **Exception Handling:** Global `@ControllerAdvice` to return standardized JSON error responses (400, 404, 500) instead of raw stack traces.

### 🎨 Frontend (React 19 + TypeScript + Vite)

- **Feature-Based Architecture:** Files are grouped by feature (`features/auth`, `features/projects`, `features/tasks`) rather than technical type. This makes the codebase easier to navigate and scale.
- **State Management:**
  - **TanStack Query (React Query):** Handles server state (fetching, caching, synchronizing data). Eliminates the need for manual `useEffect` fetching and loading states.
  - **Zustand:** Lightweight global state management for Authentication (User Token & Profile).
- **UI/UX:** Built with **Tailwind CSS** for rapid, responsive styling. Uses **Lucide React** for consistent iconography.
- **Forms:** **React Hook Form** manages complex form validation and submission logic efficiently.

### 🐳 DevOps & Deployment

- **Dockerized:** The entire stack (Frontend, Backend, Database) is containerized.
- **Nginx Reverse Proxy:** Serves the React frontend and proxies `/api` requests to the backend, eliminating CORS issues and simulating a production environment.
- **Hot Reloading:** Vite provides instant feedback during development.

---

## 🛠️ Tech Stack

| Component    | Technology                                                        |
| :----------- | :---------------------------------------------------------------- |
| **Backend**  | Java 17, Spring Boot 3.4, Spring Security, JWT, Spring Data JPA   |
| **Frontend** | React 19, TypeScript, Vite, Tailwind CSS, TanStack Query, Zustand |
| **Database** | MySQL 8.0                                                         |
| **DevOps**   | Docker, Docker Compose, Nginx                                     |

---

## ✨ Features

### 🔐 Authentication

- User Registration & Login (JWT-based).
- Secure password storage using BCrypt.
- Automatic token attachment via Axios interceptors.
- Auto-logout on token expiration.

### 📂 Project Management

- Create, Read, and Delete Projects.
- Visual **Progress Bar** (0-100%) calculated dynamically based on task completion.
- Project Details view with summary statistics.

### ✅ Task Management

- **CRUD Operations:** Create, Read, Update, Delete tasks.
- **Status Workflow:** `PENDING` → `IN_PROGRESS` → `COMPLETED`.
- **Validation:** Due dates must be in the future.
- **Pagination & Filtering:** Filter tasks by status and search by title.
- **Edit Mode:** Inline editing for task details without leaving the page.

---

## 🚀 How to Run (Recommended: Docker)

The easiest way to run the application is using Docker Compose. This sets up the Database, Backend, and Frontend automatically.

### Prerequisites

- Docker & Docker Compose installed.

### Steps

1.  **Clone the repository:**

    ```bash
    git clone <your-repo-link>
    cd <your-repo-folder>
    ```

2.  **Run the application:**

    ```bash
    docker compose up --build -d
    ```

3.  **Access the App:**
    - **Frontend:** [http://localhost](http://localhost)
    - **Backend API:** [http://localhost:8080/api](http://localhost:8080/api)

4.  **Stop the App:**
    ```bash
    docker compose down
    ```

---

## 💻 How to Run (Manual / Development)

If you want to run the services individually without Docker:

### 1. Database Setup

- Ensure MySQL is running on port `3306`.
- Create a database named `hahn_db`.
- Update `src/main/resources/application.properties` with your MySQL username/password.

### 2. Backend (Spring Boot)

```bash
cd hahn_backend
./mvnw spring-boot:run
```
