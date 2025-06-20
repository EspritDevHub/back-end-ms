FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml mvnw ./
COPY .mvn .mvn

RUN chmod +x mvnw \
 && ./mvnw dependency:go-offline

COPY src ./src

RUN ./mvnw clean package -DskipTests \
 && ls -lh target/*.jar 
 
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/evaluation-feedback-service-0.0.1-SNAPSHOT.jar evaluation-feedback-service.jar
EXPOSE 9099
ENTRYPOINT ["java", "-jar", "evaluation-feedback-service.jar"]
