FROM maven:3.9-eclipse-temurin-17 AS backend-build
WORKDIR /app

COPY pom.xml .
COPY src ./src
COPY ClientApp/package*.json ./ClientApp/
COPY ClientApp/ ./ClientApp

RUN mvn clean package -DskipTests


FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

RUN mkdir -p /app/data

COPY --from=backend-build /app/target/*.jar app.jar

EXPOSE 8080
VOLUME /app/data

ENTRYPOINT ["java", "-jar", "app.jar"]