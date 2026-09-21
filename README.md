# Pedidos360

Proyecto académico de **Desarrollo Cloud Native I (DSY1107)**. Integra Angular, Spring Boot, OAuth 2.0/OIDC con PKCE, Microsoft Entra External ID, JWT y AWS API Gateway.

## Contenido

- `frontend/`: Angular 18, MSAL, catálogo y pedidos responsive.
- `backend/`: Spring Boot 3.3, Java 17, JPA, Resource Server JWT y H2/PostgreSQL.
- `docs/`: configuración de Entra, AWS, pruebas y demostración.
- `docker-compose.yml`: entorno local con PostgreSQL.

## Inicio local sin login

El modo local sirve para comprobar el CRUD antes de disponer del tenant. Nunca debe utilizarse en AWS.

```bash
docker compose up --build
```

Abre `http://localhost:8081`. Salud API: `http://localhost:8080/api/health`.

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
cd ../frontend && npm ci && npm run build
```

Los tests cubren health público, validación y respuestas `200`, `401` y `403`.

## Estado real

- Código local: implementado.
- Identificadores Entra/AWS: deben reemplazarse con los de tu cuenta.
- Despliegue cloud: debe realizarse siguiendo `docs/`.
- Evidencias: usa `docs/demo-presentacion.md`.

Nunca subas `.env`, claves AWS, contraseñas, tokens, certificados privados ni Client Secrets a GitHub.
