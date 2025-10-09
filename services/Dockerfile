# Build the JAR
FROM maven:3.9.9-eclipse-temurin-21 AS build

# Set working directory inside the container
WORKDIR /app

# Copy only poms and src code
COPY pom.xml .
COPY runner/pom.xml runner/
COPY appointment/pom.xml appointment/
COPY common/pom.xml common/
COPY database/pom.xml database/
COPY security/pom.xml security/

COPY runner/src runner/src
COPY appointment/src appointment/src
COPY common/src common/src
COPY database/src database/src
COPY security/src security/src

# Build the runner module and all dependencies
RUN mvn clean package -pl runner -am -DskipTests

# Use an official lightweight JDK runtime as the base image
FROM eclipse-temurin:21-jdk-alpine

# Create a non-root user and group
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copy the packaged Spring Boot JAR into the container
COPY --from=build /app/runner/target/runner-1.0-SNAPSHOT.jar app.jar

# Switch to the non-root user
USER appuser

# Expose port 8080 so external services can connect
EXPOSE 8080

# Run the jar when the container starts
ENTRYPOINT ["java","-jar","app.jar"]