# Build the JAR
FROM maven:3.9.9-eclipse-temurin-21 AS build

# Set working directory inside the container
WORKDIR /app

# Copy the whole services folder
COPY . .

# Build the runner module and all dependencies
RUN mvn clean package -pl runner -am -DskipTests

# Use an official lightweight JDK runtime as the base image
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copy the packaged Spring Boot JAR into the container
COPY --from=build /app/runner/target/runner-1.0-SNAPSHOT.jar app.jar

# Expose port 8080 so external services can connect
EXPOSE 8080

# Run the jar when the container starts
ENTRYPOINT ["java","-jar","app.jar"]