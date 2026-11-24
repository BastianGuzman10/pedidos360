# Pedidos360

Proyecto académico de **Desarrollo Cloud Native I (DSY1107)**. Integra Angular, Spring Boot, PostgreSQL, RabbitMQ y Apache Kafka, además de OAuth 2.0/OIDC con PKCE, Microsoft Entra External ID, JWT y AWS API Gateway.

## Contenido

- `frontend/`: Angular 18, MSAL, catálogo y pedidos responsive.
- `backend/`: Spring Boot 3.3, Java 17, JPA, Resource Server JWT y H2/PostgreSQL.
- `docs/`: configuración de Entra, AWS, pruebas y demostración.
- `notification-service/`: consumidor RabbitMQ para notificaciones.
- `reporting-service/`: consumidor Kafka que calcula métricas en tiempo real.
- `audit-service/`: consumidor Kafka que mantiene la trazabilidad de eventos.
- `kafka-admin-service/`: API para administrar tópicos y consumer groups.
- `docker-compose.yml`: entorno local completo con PostgreSQL, RabbitMQ, Zookeeper, Kafka y Kafka UI.

## Inicio local sin login

El modo local sirve para comprobar el CRUD antes de disponer del tenant. Nunca debe utilizarse en AWS.

```bash
docker compose up --build
```

Abre `http://localhost:8081`. Salud API: `http://localhost:8080/api/health`.

Consolas y APIs de la evaluación Kafka:

- RabbitMQ Management: `http://localhost:15672`.
- Kafka UI: `http://localhost:8086`.
- Métricas: `http://localhost:8083/api/reportes/metricas`.
- Auditoría: `http://localhost:8084/api/auditoria/eventos`.
- Administración Kafka: `http://localhost:8085/api/kafka/topics`.

Sin Docker, ejecuta `mvn spring-boot:run -Dspring-boot.run.profiles=local` dentro de `backend`, y `npm ci` seguido de `npm start` dentro de `frontend`.

## Producción segura

En producción `AUTH_ENABLED=true` y son obligatorios:

- `OAUTH2_ISSUER_URI`: claim `iss` exacto de un access token válido.
- `OAUTH2_AUDIENCE`: Client ID de `Pedidos360-API`.
- `CORS_ALLOWED_ORIGINS`: URL final del frontend.

Completa `frontend/src/environments/environment.production.ts` con Client ID, authority, scope y URL de API Gateway. No agregues Client Secret: una SPA usa Authorization Code + PKCE.

## Verificación

```bash
cd backend && mvn test
cd ../reporting-service && mvn test
cd ../audit-service && mvn test
cd ../kafka-admin-service && mvn test
cd ../frontend && npm ci && npm run build
```

Consulta `docs/evaluacion-3-kafka.md` para la arquitectura, tópicos, endpoints y guion de demostración.

## Estado real

- Código local: implementado.
- Identificadores Entra/AWS: deben reemplazarse con los de tu cuenta.
- Despliegue cloud: debe realizarse siguiendo `docs/`.
- Evidencias: usa `docs/demo-presentacion.md`.

Nunca subas `.env`, claves AWS, contraseñas, tokens, certificados privados ni Client Secrets a GitHub.
