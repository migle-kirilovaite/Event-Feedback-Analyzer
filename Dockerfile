FROM maven:3.9-eclipse-temurin-17 AS backend-build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM node:18 AS frontend-build
WORKDIR /app
COPY ClientApp/package*.json ./
RUN npm install
COPY ClientApp/ .
RUN npm run build

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

RUN mkdir -p /app/data

COPY --from=backend-build /app/target/*.jar app.jar
COPY --from=frontend-build /app/build ./static

EXPOSE 8080

VOLUME /app/data

ENTRYPOINT ["java", "-jar", "app.jar"]