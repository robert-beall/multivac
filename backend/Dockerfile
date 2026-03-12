# Stage 1: Build the application
FROM gradle:jdk-21-and-24-corretto-al2023 AS build
WORKDIR /app
COPY build.gradle .
COPY settings.gradle .
COPY src ./src
RUN gradle bootJar

# Stage 2: Run the application
FROM eclipse-temurin:21-noble
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]