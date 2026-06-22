# 🏛️ Smart University Campus Management System

A Java Swing desktop application that integrates academic operations, campus facilities, and student services into a unified platform — simulating real-world campus activities while demonstrating advanced object-oriented programming concepts.

> **Course:** Object Oriented Programming
> **Institution:** COMSATS University Islamabad  
> **Semester:** Spring 2026

---

## 📋 Table of Contents

- [About](#about)
- [Features](#features)
- [OOP Concepts Demonstrated](#oop-concepts-demonstrated)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Default Login Credentials](#default-login-credentials)
- [Role-Based Access](#role-based-access)
- [Data Persistence](#data-persistence)
- [Team](#team)

---

## About

This system manages the complete operations of a university campus including departments, courses, students, classrooms, labs, facilities (libraries, cafeterias, hostels), and service units (transport, security, health center). It features a role-based login system, an interactive campus map with color-coded statuses, CRUD operations for all entities, timetable/schedule management, automated report generation, and dynamic conflict resolution for scheduling.

---

## Features

### 🎓 Academic Management
- **Departments** — Create and manage departments with student/course capacity limits, assign classrooms, add students and courses to departments
- **Courses** — Full CRUD with schedule assignment, classroom allocation, teacher assignment, and assignment management
- **Students** — Registration with auto-generated login accounts, course enrollment, and semester tracking
- **Classrooms & Labs** — Manage academic spaces with capacity, projector availability, room types, and equipment tracking

### 🏢 Campus Facilities
- **Library** — Book management with add/remove/issue/return operations, employee tracking, and usage reports
- **Cafeteria** — Menu item management, seating capacity, and staff tracking
- **Hostel** — Room assignment/unassignment for students, occupancy tracking, and room availability

### 🚌 Service Units
- **Transport Service** — Route management, express routes for peak hours, dynamic schedule generation, and vehicle/driver tracking
- **Security Service** — Guard management, shift scheduling (Morning/Afternoon/Evening/Night), security level configuration, and medical emergency notifications
- **Health Center** — Doctor and bed tracking, medicine inventory, and occupied/available bed management

### 🗺️ Interactive Campus Map
- Visual dashboard displaying all campus buildings and services as clickable cards
- **Three-state status cycling**: OPERATIONAL (green) → BUSY (orange) → CLOSED (red) → OPERATIONAL
- Real-time status updates with color-coded indicators

### 📊 Reports & Timetables
- **System-wide audit report** covering all metrics: students, courses, departments, facilities, services, and operational costs
- **Department performance reports** via the `Reportable` interface
- **Library usage reports** via the `Reportable` interface
- **Course timetable** with schedule, classroom, teacher, and enrollment details
- **Transport schedule** with route information and express route status
- **Assignment tracker** showing all due assignments across courses

### 🔒 Role-Based Access Control
- Three user roles with different permission levels: Admin, Teacher, Student
- Secure login/logout with session management
- Dynamic sidebar navigation that adapts to the logged-in user's role

### 📐 Dynamic Scheduling & Conflict Resolution
- **Schedule conflict detection** — Detects when two courses share the same classroom and time slot
- **Automatic rescheduling** — Resolves conflicts by finding the next available time slot
- **Classroom rescheduling** — When a classroom becomes unavailable, affected courses are reassigned to available rooms
- **Transport peak hour adjustment** — Express routes are added/removed based on time of day

### 💾 Auto-Save & Data Backup
- Automatic save every 5 minutes via a background timer
- Data saved on logout and application exit (shutdown hook)
- Backup `.dat` file created before every save to prevent data loss
- Corrupted/empty file handling with automatic fallback to backup

---

## OOP Concepts Demonstrated

| Concept | Implementation |
|---|---|
| **Abstract Classes** | `CampusEntity` (base), `AcademicUnit`, `Facility`, `ServiceUnit`, `User` — all with abstract or overridden methods |
| **Inheritance** | `Department`, `Classroom`, `Lab` extend `AcademicUnit`; `Library`, `Cafeteria`, `Hostel` extend `Facility`; `SecurityService`, `HealthCenter`, `TransportService` extend `ServiceUnit`; `AdminUser`, `TeacherUser`, `StudentUser` extend `User` |
| **Interfaces** | `Notifiable` (SecurityService, HealthCenter, AdminUser), `Schedulable` (Course, TransportService), `Reportable` (Department, Library) |
| **Polymorphism** | `calculateOperationalCost()` overridden in every concrete entity; `toString()` overridden in all classes for GUI display |
| **Composition** | Department *contains* Courses, Students, Classrooms; Course *contains* Students, Assignments; Library *manages* Books |
| **Aggregation** | CampusZone *aggregates* Facilities and ServiceUnits (can exist independently) |
| **Generics** | `CampusRepository<T extends Serializable>` — type-safe collection manager for all entity types |
| **Static Members** | `totalStudents`, `courseCount`, `totalFacilityUsage` tracked system-wide |
| **Serialization** | All model classes implement `Serializable` for `.dat` file persistence |
| **Exception Handling** | `IOException`, `ClassNotFoundException`, `StreamCorruptedException`, `EOFException` handled in file operations |
| **Encapsulation** | All fields private with validated getters/setters throughout |

---

## Project Structure

```
src/com/campus/
│
├── model/                      # Domain model classes
│   ├── CampusEntity.java           # Abstract base class (entityID, name, location)
│   ├── AcademicUnit.java           # Abstract: capacity, headName, isActive
│   ├── Department.java             # Reportable — manages students, courses, classrooms
│   ├── Classroom.java              # Room type, projector, availability
│   ├── Lab.java                    # Lab type, technician, equipment
│   ├── Facility.java               # Abstract: maintenance cost, usage frequency
│   ├── Library.java                # Reportable — book management, issue/return
│   ├── Cafeteria.java              # Menu items, seating, staff
│   ├── Hostel.java                 # Room assignment, occupancy
│   ├── ServiceUnit.java            # Abstract: staff, hours, cost
│   ├── SecurityService.java        # Notifiable — guards, shifts, emergency handling
│   ├── HealthCenter.java           # Notifiable — doctors, beds, medicines
│   ├── TransportService.java       # Schedulable — routes, peak hours, express routes
│   ├── CampusZone.java             # Aggregates facilities and service units
│   ├── Student.java                # Enrollment, course management
│   ├── Course.java                 # Schedulable — assignments, classroom, students
│   ├── Assignment.java             # Deadline tracking with validation
│   ├── Book.java                   # ISBN, author, genre
│   ├── User.java                   # Abstract base for authentication
│   ├── AdminUser.java              # Notifiable — full system access
│   ├── TeacherUser.java            # Manages assigned courses
│   └── StudentUser.java            # Links to Student record
│
├── interfaces/                 # Interface contracts
│   ├── Notifiable.java             # sendNotification(String)
│   ├── Schedulable.java            # generateSchedule()
│   └── Reportable.java             # generateReport()
│
├── repository/                 # Data access layer
│   ├── CampusRepository.java       # Generic repository: add, remove, find, getAll
│   ├── DataManager.java            # Central data hub — all repositories + save/load
│   └── FileManagement.java         # .dat file I/O with backup and exception handling
│
├── service/                    # Business logic
│   └── AuthenticationService.java  # Login, logout, role checking, session management
│
├── gui/                        # Swing GUI panels
│   ├── MainFrame.java              # JFrame with CardLayout navigation, auto-save timer
│   ├── LoginPanel.java             # Authentication UI
│   ├── DashboardPanel.java         # Sidebar navigation, role-based menu, header
│   ├── UITheme.java                # Centralized colors, fonts, styled components
│   ├── FormUtility.java            # Reusable GridBagLayout form builder
│   ├── CampusMapPanel.java         # Interactive building cards with status cycling
│   ├── StudentCrudPanel.java       # Student CRUD + enrollment
│   ├── CourseCrudPanel.java        # Course CRUD + assignments + scheduling
│   ├── DepartmentCrudPanel.java    # Department CRUD + conflict resolution
│   ├── FacilityCrudPanel.java      # Library/Cafeteria/Hostel CRUD
│   ├── ServiceUnitCrudPanel.java   # Transport/Security/HealthCenter CRUD
│   ├── LabCrudPanel.java           # Lab CRUD
│   ├── ClassroomCrudPanel.java     # Classroom CRUD
│   ├── CampusZoneCrudPanel.java    # Zone management
│   ├── UserCrudPanel.java          # User account management
│   ├── TimetablePanel.java         # Course/Transport/Assignment schedules
│   └── ReportsPanel.java           # System-wide audit report
│
└── main/
    └── Main.java                   # Application entry point
```

---

## Getting Started

### Prerequisites

- **Java JDK 8** or higher
- Any IDE (IntelliJ IDEA, Eclipse, VS Code) or command line

### Compile & Run

```bash
# Clone the repository
git clone https://github.com/alisaimm/University-Management-System.git
cd University-Management-System

# Compile
javac -d bin -sourcepath src src/com/campus/main/Main.java

# Run
java -cp bin com.campus.main.Main
```

### Run from IDE

1. Open the project in IntelliJ IDEA / Eclipse
2. Mark `src/` as the source root
3. Run `com.campus.main.Main`

---

## Default Login Credentials

The system seeds default accounts on first launch:

| Role | Username | Password |
|---|---|---|
| Admin | `admin` | `admin123` |
| Teacher | `teacher1` | `pass123` |

> Student accounts are created automatically when you register a new student — the student's email is used as their username.

---

## Role-Based Access

| Feature | Admin | Teacher | Student |
|---|:---:|:---:|:---:|
| Campus Map | ✅ | ✅ | ✅ |
| Timetable & Reports | ✅ | ✅ | ✅ |
| Manage Students | ✅ | ✅ | ❌ |
| Manage Courses | ✅ | ✅ | ❌ |
| Manage Facilities | ✅ | ❌ | ❌ |
| Manage Service Units | ✅ | ❌ | ❌ |
| Manage Departments | ✅ | ❌ | ❌ |
| Manage Labs & Classrooms | ✅ | ❌ | ❌ |
| Manage Campus Zones | ✅ | ❌ | ❌ |
| Manage Users | ✅ | ❌ | ❌ |

---

## Data Persistence

All system data is persisted using Java Object Serialization:

- **Save location:** `data/system_state.dat`
- **Backup file:** `data/system_state.dat.bak` (created before every save)
- **Auto-save:** Every 5 minutes via a background `javax.swing.Timer`
- **Manual save:** Triggered on logout and application exit
- **Recovery:** If the main file is corrupted or empty, the system automatically falls back to the backup file

---

## Team

| Name | GitHub |
|---|---|
| Ali Saim Salehzadeh | [@alisaimm](https://github.com/alisaimm) |
| Muhammad Hamza Shakeel | [@mhamza-shakeel](https://github.com/mhamza-shakeel) |

---

<p align="center">
  Built with ☕ Java Swing — COMSATS University Islamabad
</p>
