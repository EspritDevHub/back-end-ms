# Étape 1 : construction du WAR (multi-stage build)
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw dependency:go-offline

COPY src ./src
RUN ./mvnw clean package -DskipTests

# Étape 2 : runtime avec Tomcat
FROM tomcat:10.1-jdk17-corretto-alpine
WORKDIR /usr/local/tomcat/webapps

# Supprime l'application par défaut et copie votre WAR
RUN rm ROOT.war && rm -rf ROOT
COPY --from=builder /app/target/evaluation-feedback-service-0.0.1-SNAPSHOT.war ROOT.war

EXPOSE 8080

# Le Tomcat embarqué lancera automatiquement ROOT.war
CMD ["catalina.sh", "run"]
