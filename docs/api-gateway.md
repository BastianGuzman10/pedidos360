# AWS API Gateway - Pedidos360

Usa una **HTTP API** para disponer del authorizer JWT nativo.

1. Verifica `http://IP_EC2:8080/api/health`.
2. Crea una HTTP API y una integración HTTP proxy hacia `http://IP_O_DNS_EC2:8080`.
3. Crea las rutas:

| Método | Ruta | Auth |
|---|---|---|
| GET | `/api/health` | Pública |
| GET, POST | `/api/productos` | JWT |
| GET, PUT, DELETE | `/api/productos/{id}` | JWT |
| GET, POST | `/api/pedidos` | JWT |
| GET, DELETE | `/api/pedidos/{id}` | JWT |
| PUT | `/api/pedidos/{id}/estado` | JWT |

## Authorizer

- Nombre: `Pedidos360JwtAuthorizer`.
- Identity source: `$request.header.Authorization`.
- Issuer: claim `iss` exacto del access token.
- Audience: Client ID de `Pedidos360-API`.
- Asócialo a todas las rutas excepto `/api/health`.

El backend vuelve a validar firma, issuer, audience y scope como defensa en profundidad.

## CORS

- Origins: `http://localhost:4200` y URL HTTPS final.
- Methods: `GET, POST, PUT, DELETE, OPTIONS`.
- Headers: `Authorization, Content-Type`.
- No uses `*` junto con credenciales.

## Pruebas

```bash
curl -i https://<API>/api/health
curl -i https://<API>/api/productos
curl -i -H "Authorization: Bearer TOKEN_INVALIDO" https://<API>/api/productos
curl -i -H "Authorization: Bearer <ACCESS_TOKEN_VALIDO>" https://<API>/api/productos
```

Resultados: `200`, `401`, `401` y `200`. Para `403`, usa un JWT válido sin `access_as_user`. Oculta los tokens en las capturas.
