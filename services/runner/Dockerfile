# Build the JAR
FROM maven:3.9.4-eclipse-temurin-17 AS build

# Set working directory inside the container
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the JAR
RUN mvn clean package -DskipTests

# Use an official lightweight JDK runtime as the base image
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copy the packaged Spring Boot JAR into the container
COPY --from=build /app/target/runner-1.0-SNAPSHOT.jar app.jar

# Expose port 8080 so external services can connect
EXPOSE 8080

# Run the jar when the container starts
ENTRYPOINT ["java","-jar","app.jar"]