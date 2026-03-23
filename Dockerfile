# ── Stage 1: build ────────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /workspace

# Copy Maven wrapper + pom trước để tận dụng layer cache
COPY mvnw mvnw.cmd ./
COPY .mvn .mvn
COPY pom.xml ./
RUN ./mvnw -q dependency:go-offline -DskipTests

# Copy source và build (test dùng H2, không cần MySQL)
COPY src src
RUN ./mvnw -q package -DskipTests

# ── Stage 2: runtime ──────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring

COPY --from=builder /workspace/target/*.jar app.jar

EXPOSE 80

ENTRYPOINT ["java", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-DSERVER_PORT=80", \
  "-jar", "app.jar"]
