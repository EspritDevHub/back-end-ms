# Étape 1 : Build de l'application
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Étape 2 : Image d'exécution
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
WORKDIR /app
COPY --from=build /app/target/note-management-microservice-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9097

ENTRYPOINT ["java", "-jar", "app.jar"]
