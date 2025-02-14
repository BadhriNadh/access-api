# Use an official Gradle image as a builder
FROM gradle:8.3-jdk17 AS builder

WORKDIR /app

# Copy the Gradle wrapper and build scripts first for caching dependencies
COPY gradle gradle
COPY gradlew .
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Download dependencies
RUN ./gradlew dependencies --no-daemon

# Copy the entire project
COPY . .

# Build the application
RUN ./gradlew bootJar --no-daemon

# Use a minimal image to run the application
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/build/libs/*.jar access.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "access.jar"]

LABEL version="0.0.1"
LABEL maintainer="Badhri Nadh Arja <arja.badhrinadh@gmail.com>"
LABEL description="Access Application"
