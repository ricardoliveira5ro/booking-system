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
├── 📂 common/
│   ├── 📂 aop/
│   └── 📂 exception/
├── 📂 database/
│   ├── 📂 entity/
│   └── 📂 resources/
│       ├── 📂 db/
│       │   ├── 📂 callbacks/
│       │   ├── 📂 migration/
│       │   └── 📂 rollbacks/
│       └── 📄 application-database.yml
├── 📂 security/
│   └── 📂 security/     
│       └── 📄 SecurityConfiguration.java
├── 📂 appointment/
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
│       └── 📄 application.yml                          # Global default
├── 📂 runner/
│   ├── 📂 src/
│   │   └── 📄 AppointmentRunner.java                   # Global Services Runner
│   └── 📂 resources/
│       └── 📄 application.yml                          # Global default
├── 📄 Dockerfile                                       # Build and package backend as a Docker image for deployment
├── 📄 pom.xml
└── ...
```