# Step 1: Build WAR
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw dependency:go-offline

COPY src ./src
RUN ./mvnw clean package -DskipTests

# Step 2: Runtime with Tomcat
FROM tomcat:10.1-jdk17-temurin-alpine
WORKDIR /usr/local/tomcat/webapps

# Clean default app and deploy your WAR
RUN rm ROOT.war && rm -rf ROOT
COPY --from=builder /app/target/evaluation-feedback-service-0.0.1-SNAPSHOT.war feedback-service.war

EXPOSE 9099

CMD ["catalina.sh", "run"]
