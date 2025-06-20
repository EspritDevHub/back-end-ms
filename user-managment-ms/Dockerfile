# Step 1: Build the application using Maven
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy Maven project files
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Make mvnw executable
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy the source code
COPY src ./src

# Build the JAR file
RUN ./mvnw clean package -DskipTests

# Step 2: Run the app with a lightweight JRE
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the built JAR from the builder
EXPOSE 9090
COPY --from=builder /app/target/suer-management-microservice-0.0.1-SNAPSHOT.jar  user-service.jar


# Run the application
ENTRYPOINT ["java", "-jar", "user-service.jar"]
