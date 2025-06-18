FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml mvnw .mvn/ ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline

COPY src/ ./src/

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine AS runtime

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

RUN chown appuser:appgroup app.jar
USER appuser

EXPOSE 9097

ENTRYPOINT ["java", "-jar", "app.jar"]
