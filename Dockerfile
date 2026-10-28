# ==============================================================================
# Multi-Stage Dockerfile for Spring Boot E-Commerce Application (Java 21)
# ==============================================================================

# ------------------------------------------------------------------------------
# Stage 1: Build Stage using Maven and OpenJDK 21
# ------------------------------------------------------------------------------
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

WORKDIR /build

# Cache Maven dependencies by copying pom.xml first
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy application source code and package executable JAR
COPY src ./src
RUN mvn clean package -DskipTests -B

# ------------------------------------------------------------------------------
# Stage 2: Minimal Production JRE Runtime
# ------------------------------------------------------------------------------
FROM eclipse-temurin:21-jre-alpine

# Set metadata labels
LABEL maintainer="DevOps Team"
LABEL description="DevOps E-Commerce Spring Boot Web Application"
LABEL version="1.0.0"

# Create a dedicated non-root application user for container security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copy the generated JAR file from the builder stage
COPY --from=builder /build/target/devops-ecommerce-1.0.0.jar app.jar

# Ensure appropriate file permissions
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Expose standard Spring Boot HTTP port
EXPOSE 8080

# Configure JVM memory ergonomics and run container
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
