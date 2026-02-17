FROM gradle:8-jdk21 AS build
WORKDIR /app

COPY .. .
RUN gradle build --no-daemon

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

COPY --from=build /app/build/libs/*.jar usuario.jar

EXPOSE 8080
CMD ["java", "-jar", "usuario.jar"]