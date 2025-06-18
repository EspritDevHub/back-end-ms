# Étape 1 : Build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Étape 2 : Execution
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
WORKDIR /app
COPY --from=build /app/target/seance_note-management-microservice-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9092

ENTRYPOINT ["java", "-jar", "app.jar"]
