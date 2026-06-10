FROM gradle:8.14-jdk21 AS builder
WORKDIR /app

COPY build.gradle settings.gradle gradle.properties ./

RUN gradle dependencies --no-daemon

COPY src src

RUN gradle bootJar --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app

RUN groupadd --system appgroup && useradd --system --gid appgroup --create-home appuser

ENV DJL_CACHE_DIR=/app/.djl_cache

RUN mkdir -p /app/output /app/logs /app/.djl_cache && chown -R appuser:appgroup /app

COPY --from=builder /app/build/libs/*.jar app.jar

COPY --from=builder /app/src/main/resources resources/

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]