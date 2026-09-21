# Arquitectura

```text
Usuario → Angular/MSAL → Microsoft Entra External ID
   │              obtiene access token JWT
   └── HTTPS + Bearer JWT → AWS API Gateway → Spring Boot/BFF → PostgreSQL
                                  │                  │
                             valida JWT       valida JWT y scope
```

El backend funciona como BFF/API y separa los contextos de productos y pedidos en controladores, repositorios y modelos independientes. API Gateway publica rutas explícitas por contexto. Ambos dominios pueden separarse posteriormente en microservicios sin cambiar el contrato del frontend.

Controles: PKCE, ausencia de secretos en la SPA, doble validación JWT, comprobación `iss`/`aud`/`scp`, CORS explícito, total calculado en backend y credenciales por variables de entorno.
