# Step 1: Build the application using Maven
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy Maven project files
COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline

# Copy the source code
COPY src ./src

# Build the JAR file
RUN ./mvnw clean package -DskipTests

# Step 2: Run the app with a lightweight JRE
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the built JAR from the builder
COPY --from=builder /app/target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
