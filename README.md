# 🚀 Task Management System
### Hahn Software Morocco — End of Studies Internship 2026

## 🛠 Tools & Technologies Used

### Backend
![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-Auth-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)

### Frontend
![React](https://img.shields.io/badge/React-19-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)
![TypeScript](https://img.shields.io/badge/TypeScript-5.0-3178C6?style=for-the-badge&logo=typescript&logoColor=white)
![Vite](https://img.shields.io/badge/Vite-Build-646CFF?style=for-the-badge&logo=vite&logoColor=white)
![TailwindCSS](https://img.shields.io/badge/Tailwind_CSS-3.4-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white)
![TanStack Query](https://img.shields.io/badge/TanStack_Query-v5-FF4154?style=for-the-badge&logo=reactquery&logoColor=white)
![Zustand](https://img.shields.io/badge/Zustand-State-orange?style=for-the-badge)

### DevOps & Infrastructure
![Docker](https://img.shields.io/badge/Docker-Container-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Nginx](https://img.shields.io/badge/Nginx-Proxy-009639?style=for-the-badge&logo=nginx&logoColor=white)
![Git](https://img.shields.io/badge/Git-VCS-F05032?style=for-the-badge&logo=git&logoColor=white)

---

## 📖 Project Overview

This **Full Stack Task Management System** was designed and built as a technical test for the **Hahn Software** internship program. It addresses the need for a robust project tracking tool where users can create projects, assign tasks, and visualize progress in real-time.

The application follows a **Microservice-ready Monolithic Architecture**, ensuring that the codebase is modular, scalable, and easy to maintain.

---

## 🏗️ Architecture & Design Choices

### 1. Backend Architecture (Spring Boot)
The backend is built using a **Layered Architecture** to enforce separation of concerns.
* **Controller Layer:** Handles HTTP requests and response formatting.
* **Service Layer:** Contains business logic (e.g., calculating progress, validating due dates).
* **Repository Layer:** Manages data persistence with MySQL using JPA/Hibernate.
* **Security:** Stateless authentication using **JWT** filters.

#### 📐 Class Diagram (UML)
Below is the core domain model representing the relationship between Users, Projects, and Tasks.

![Class Diagram](./docs/img.png)


### 2. Frontend Architecture (React)
The frontend uses a **Feature-Based Folder Structure**, keeping all logic related to a feature (API, Components, Types) in one place.

![Frontend's ARCH ](./docs/img_1.png)

* **State Management:**
    * **Zustand:** Used for global client state (Authentication, User Profile) due to its lightweight nature.
    * **TanStack Query:** Used for server state (Projects, Tasks). It handles caching, background refetching, and loading states automatically, replacing complex `useEffect` chains.
* **Routing:** React Router v6 protected routes (redirects to Login if no token exists).

### 3. DevOps Strategy (Docker & Nginx)
The application is fully containerized to ensure it runs identically on any machine.
* **Nginx Reverse Proxy:** Serves the React static files and forwards `/api` requests to the Backend container. This mimics a real production environment and eliminates CORS issues.
* **Docker Compose:** Orchestrates the database, backend, and frontend containers with a single command.

---

## 📸 UI Screenshots

### Login/Register Page  
![Dashboard Screenshot](./docs/login.png)

### Dashboard & Project List
![Dashboard Screenshot](./docs/projects.png)

### Task Management & Editing
*(Add a screenshot of the Task List with the Edit Form open)*
![Task Edit Screenshot](./docs/tasks.png)

---

## 🚀 Getting Started

You can run this project in two ways: using **Docker (Recommended)** or **Manually**.

### Option 1: Quick Start with Docker 🐳
This requires Docker and Docker Compose to be installed.

1.  **Clone the repository**
    ```bash
    git clone https://github.com/Salahjb/Hahn_project
    cd Hahn_project
    ```

2.  **Run the application**
    ```bash
    docker compose up --build -d
    ```

3.  **Access the App**
    * Frontend: [http://localhost](http://localhost)
    * Backend API: [http://localhost:8080/api](http://localhost:8080/api)

### Option 2: Manual Setup 💻

#### Backend
1.  Navigate to `hahn_backend`.
2.  Update `application.properties` with your local MySQL credentials.
3.  Run:
    ```bash
    ./mvnw spring-boot:run
    ```

#### Frontend   
1.  Navigate to `hahn_frontend`.
2.  Install dependencies and run:
    ```bash
    npm install
    npm run dev
    ```
3.  *Note:* In manual mode, ensure `src/lib/axios.ts` points to `http://localhost:8080/api`.

---

## ✅ Key Features Implemented

- [x] **Secure Auth:** JWT Login & Registration with BCrypt hashing.
- [x] **Projects:** Create projects and view real-time progress calculations.
- [x] **Tasks:** CRUD operations with Status workflow (Pending → In Progress → Completed).
- [x] **Validation:** Backend & Frontend validation (e.g., prevent past due dates).
- [x] **UX:** Inline editing, loading skeletons, and responsive design.

---

## 👤 Author

**Salaheddine Eljably**
* **Role:** Full Stack Developer
* **Context:** Hahn Software Morocco Internship Test 2026
* **LinkedIn:** [Salaheddine Eljably](https://www.linkedin.com/in/salaheddine-eljably-95aab0288/)

