# 1. Use an official lightweight JDK runtime as the base image
FROM eclipse-temurin:21-jdk-alpine

# 2. Set working directory inside the container
WORKDIR /app

# 3. Copy the packaged Spring Boot JAR into the container
COPY target/runner-1.0-SNAPSHOT.jar app.jar

# 4. Expose port 8080 so external services can connect
EXPOSE 8080

# 5. Run the jar when the container starts
ENTRYPOINT ["java","-jar","app.jar"]