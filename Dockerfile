FROM gradle:8.14-jdk21 AS builder
WORKDIR /app
COPY build.gradle settings.gradle lombok.config ./
COPY gradle ./gradle
RUN gradle dependencies --no-daemon
COPY src ./src
RUN gradle build -x test --no-daemon

FROM alpine:3.20 AS jattach
ARG JATTACH_VERSION=v2.2
ARG JATTACH_SHA256=acd9e17f15749306be843df392063893e97bfecc5260eef73ee98f06e5cfe02f
RUN wget -qO /tmp/jattach.tgz \
      https://github.com/jattach/jattach/releases/download/${JATTACH_VERSION}/jattach-linux-x64.tgz \
    && echo "${JATTACH_SHA256}  /tmp/jattach.tgz" | sha256sum -c - \
    && tar -xzf /tmp/jattach.tgz -C /usr/local/bin \
    && chmod +x /usr/local/bin/jattach

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
COPY --from=jattach /usr/local/bin/jattach /usr/local/bin/jattach
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=55.0", "-XX:MaxMetaspaceSize=224m", "-XX:+HeapDumpOnOutOfMemoryError", "-XX:HeapDumpPath=/app/dump", "-XX:+ExitOnOutOfMemoryError", "-XX:NativeMemoryTracking=summary", "-jar", "app.jar"]