# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
./gradlew build

# Run tests
./gradlew test

# Run a single test class
./gradlew test --tests "com.acenexus.tata.eurekaservice.EurekaserviceApplicationTests"

# Build executable JAR (bootJar only; plain jar is disabled)
./gradlew bootJar

# Run locally
./gradlew bootRun
```

## Architecture

This is a **Spring Cloud Netflix Eureka Server** — the service registry for a microservices ecosystem. It runs on port 8761 by default.

### Key files

| File | Purpose |
|------|---------|
| `EurekaserviceApplication.java` | Entry point; `@EnableEurekaServer` activates the registry |
| `SecurityConfig.java` | HTTP Basic Auth — `/actuator/health` and `/eureka/**` are open; all other paths require auth |
| `src/main/resources/application-local.yml` | Local profile: Config Server and RabbitMQ Bus disabled |
| `src/main/resources/application-prod.yml` | Prod profile: imports from Config Server, enables RabbitMQ |
| `src/main/resources/logback-spring.xml` | Suppresses known Eureka Jersey3 shutdown bug logs; configures log levels via Spring properties |
| `src/test/resources/application.yml` | Test config: disables Config Server, Bus, and Eureka auto-registration |

### Profile system

The active profile is set via `SPRING_PROFILES_ACTIVE` (defaults to `local`):

- **`local`**: Standalone — Config Server and RabbitMQ Bus disabled. Only `SECURITY_USERNAME` and `SECURITY_PASSWORD` are required.
- **`prod`**: Connects to Spring Cloud Config Server (`fail-fast: true`) and RabbitMQ for Spring Cloud Bus. Requires additional env vars (see below).

### Security

`/eureka/**` and `/actuator/health` are unauthenticated. All other endpoints (including the Eureka Web UI at `/`) require HTTP Basic Auth. CSRF is disabled.

### Distributed tracing

Dependencies `micrometer-tracing-bridge-otel` and `opentelemetry-exporter-otlp` are included for OpenTelemetry-based distributed tracing integration.

### Versioning & CI/CD

JAR version is derived from the latest Git tag (`git describe --tags --abbrev=0`); falls back to `0.0.1-SNAPSHOT`. Output JAR is always named `eurekaservice.jar`.

**CI/CD**: GitHub Actions (`.github/workflows/ci.yml`) triggers on push / PR to `main`:
- test job: `./gradlew build` (compile + test)
- release job: `./gradlew bootJar` → `docker build` → Trivy scan → push `ghcr.io/acenexus/eurekaservice:<sha>` to GHCR → update `AceNexus/deploy` k8s/eurekaservice/deployment.yaml image tag
- ArgoCD detects deploy repo change → `kubectl apply` → rolling update to Kubernetes (namespace: `acenexus`)

### Environment variables

| Variable | Required for | Description |
|----------|-------------|-------------|
| `SECURITY_USERNAME` | all | HTTP Basic Auth username |
| `SECURITY_PASSWORD` | all | HTTP Basic Auth password |
| `SERVER_PORT` | all | Eureka server port (default `8761`) |
| `SERVER_HOST` | all | Instance hostname/IP for Eureka self-registration |
| `SPRING_PROFILES_ACTIVE` | all | Active profile (`local` or `prod`) |
| `CONFIG_SERVER_URI` | prod | Spring Cloud Config Server URL |
| `CONFIG_SERVER_USERNAME` | prod | Config Server auth username |
| `CONFIG_SERVER_PASSWORD` | prod | Config Server auth password |
| `RABBITMQ_HOST` | prod | RabbitMQ host |
| `RABBITMQ_PORT` | prod | RabbitMQ port (default `5672`) |
| `RABBITMQ_USERNAME` | prod | RabbitMQ username |
| `RABBITMQ_PASSWORD` | prod | RabbitMQ password |
