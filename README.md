# DevOps Helpdesk API

[![CI/CD](https://github.com/mouaadchakir/devops-helpdesk-api/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/mouaadchakir/devops-helpdesk-api/actions/workflows/ci-cd.yml)

REST API de gestion de tickets support (mini Helpdesk) avec Spring Boot.

## Stack

- Java 17
- Spring Boot 3.3.x
- Spring Security (HTTP Basic)
- Spring Data JPA
- MySQL
- Docker + Docker Compose
- GitHub Actions (CI/CD)

## Rôles et comptes par défaut

Comptes créés automatiquement au démarrage:

- `user / user123` (ROLE_USER)
- `agent / agent123` (ROLE_AGENT)
- `admin / admin123` (ROLE_ADMIN)

## Lancer l'application

### Option A: MySQL local (XAMPP)

Configurer `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/helpdesk_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=
```

Puis lancer:

```bash
./mvnw spring-boot:run
```

### Option B: MySQL Docker

```bash
docker compose up -d
./mvnw spring-boot:run
```

## Documentation API

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

> `http://localhost:8080/` retourne 404 car c'est une API (pas une interface web).

## Endpoints principaux

### Auth

- `POST /api/auth/register` (public)
- `GET /api/auth/me` (auth requis)

### Tickets

- `POST /api/tickets` (user/agent/admin)
- `GET /api/tickets` (tickets accessibles selon rôle)
- `GET /api/tickets/{ticketId}`
- `PATCH /api/tickets/{ticketId}/assign` (agent/admin)
- `PATCH /api/tickets/{ticketId}/status` (agent/admin)
- `POST /api/tickets/{ticketId}/responses` (agent/admin)

## Exemples curl (Basic Auth)

```bash
curl -u user:user123 http://localhost:8080/api/auth/me
```

```bash
curl -u user:user123 -X POST http://localhost:8080/api/tickets \
  -H "Content-Type: application/json" \
  -d "{\"title\":\"Bug login\",\"description\":\"Cannot login\",\"priority\":\"HIGH\"}"
```

## Docker image

Build local image:

```bash
docker build -t helpdesk-api:local .
```

Run:

```bash
docker run --rm -p 8080:8080 helpdesk-api:local
```

## Workflow Git recommandé

- `main`: production
- `develop`: intégration
- `feature/*`: nouvelles fonctionnalités via Pull Request
