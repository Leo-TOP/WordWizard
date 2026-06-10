# Spring Boot 3.2's Gradle plugin does not support Gradle 9 (the project wrapper),
# so the image builds with its own Gradle 8.14 + JDK 21
FROM gradle:8.14-jdk21 AS builder
WORKDIR /app

COPY build.gradle settings.gradle gradle.properties ./

RUN gradle dependencies --no-daemon

COPY src src

RUN gradle bootJar --no-daemon


FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup --system appgroup && adduser --system appuser --ingroup appgroup

RUN mkdir -p /app/output /app/logs && chown -R appuser:appgroup /app

COPY --from=builder /app/build/libs/*.jar app.jar

COPY --from=builder /app/src/main/resources resources/

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]