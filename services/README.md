# Services

Built with Java and Spring Boot and organized as a multi-module Maven project, handles all business logic, appointment management, security, database operations, and integrations with Google Calendar and email notifications.

### 🚀 Tech Stack

- Spring Boot
- Java
- Maven
- PostgreSQL (hosted on Supabase)
- Hibernate (JPA)
- Flyway
- Resend (Email Service)
- Thymeleaf templates
- Google Calendar API
- Spring Security
- Jakarta Validation API
- ModelMapper
- Docker
- JUnit 
- SonarQube

### 📁 Project Structure

```

📁 services/
├── 📂 common/                                          # Shared AOP logging and global exception handling
│   ├── 📂 aop/
│   └── 📂 exception/
├── 📂 database/                                        # JPA entities, Flyway migration scripts, and seed data
│   ├── 📂 entity/
│   └── 📂 resources/
│       ├── 📂 db/
│       │   ├── 📂 callbacks/
│       │   ├── 📂 migration/
│       │   └── 📂 rollbacks/
│       └── 📄 application-database.yml
├── 📂 security/                                        # Spring Security config and CORS management
│   └── 📂 security/     
│       └── 📄 SecurityConfiguration.java
├── 📂 appointment/                                     # Appointment endpoints, logic, and integrations
│   ├── 📂 src/
│   │   ├── 📂 config/
│   │   ├── 📂 controller/
│   │   ├── 📂 dto/
│   │   ├── 📂 repository/
│   │   ├── 📂 service/
│   │   ├── 📂 validation/
│   │   └── 📄 AppointmentApplication.java              # Service runner
│   └── 📂 resources/
│       ├── 📂 templates/
│       │   └── 📂 templates/
│       │       └── 📄 appointment-confirmation.html
│       ├── 📄 application-appointment.yml
│       └── 📄 application.yml                          
├── 📂 runner/                                          # Entry module bundling all others into a deployable Spring Boot app
│   ├── 📂 src/
│   │   └── 📄 AppointmentRunner.java                   # Global Services Runner
│   └── 📂 resources/
│       └── 📄 application.yml                          # Global default
├── 📄 Dockerfile                                       # Build and package backend as a Docker image for deployment
├── 📄 pom.xml
└── ...
```

### ⚙️ Core Concepts & Implementations

- **Flyway Schema Versioning**: 
Database migrations and seed scripts ensure consistent environments across deployments.

- **Lombok**:
Removes boilerplate code (getters, setters, constructors, etc.).

- **AOP Logging**:
Cross-cutting logging via Spring AOP for consistent tracing and debugging.

- **Spring Security**:
Configured for CORS control — only allows requests from the deployed frontend origin (and localhost in dev).

- **Google Calendar API**:
Syncs confirmed appointments directly to the barber’s Google Calendar.

- **Email Notifications (Resend + Thymeleaf)**:
Sends confirmation/cancellation emails rendered from HTML templates.

- **Validation**:
Enforces structured and valid input data across all request DTOs.

- **ModelMapper**:
Simplifies conversions between DTOs and entities with custom mappings.

- **Concurrency Handling**:
Uses synchronized blocks and ConcurrentHashMap to prevent race conditions or double bookings.

- **Testing & Code Quality**:
JUnit for unit testing, integrated with SonarQube for coverage and static analysis.

- **Docker Packaging**:
Packaged into a container image and deployed to Render.