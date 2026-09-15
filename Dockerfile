FROM gradle:8.14-jdk21 AS builder
WORKDIR /app
COPY build.gradle settings.gradle lombok.config ./
COPY gradle ./gradle
RUN gradle dependencies --no-daemon
COPY src ./src
RUN gradle build -x test --no-daemon

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=55.0", "-XX:MaxMetaspaceSize=224m", "-jar", "app.jar"]