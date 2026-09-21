# Microsoft Entra External ID - Pedidos360

## 1. Tenant

Ruta: `https://entra.microsoft.com` → **Entra ID** → **Información general** → **Administrar inquilinos** → **Crear** → **Externo**. También se puede usar la extensión oficial Microsoft Entra External ID de Visual Studio Code. Si Duoc bloquea la creación, usa una cuenta Microsoft personal o solicita el rol Tenant Creator.

Guarda como evidencia nombre, dominio principal, Tenant ID y usuarios, sin mostrar contraseñas.

## 2. Registrar `Pedidos360-API`

1. En el tenant externo abre **Registros de aplicaciones** → **Nuevo registro**.
2. Selecciona solo cuentas de este directorio.
3. En **Exponer una API**, define `api://<API_CLIENT_ID>`.
4. Agrega el scope delegado `access_as_user` y habilítalo.
5. Copia Client ID y Tenant ID.

## 3. Registrar `Pedidos360-Frontend`

1. Crea la aplicación y agrega plataforma **Single-page application**.
2. URI local: `http://localhost:4200/`.
3. Agrega después la URL HTTPS desplegada.
4. No crees Client Secret.
5. En **Permisos de API**, agrega `Pedidos360-API/access_as_user`.
6. Autoriza el Client ID de la SPA como aplicación cliente cuando el portal lo permita.

## 4. Flujo de usuarios

En **External ID** → **Flujos de usuario**, crea `Pedidos360SignUpSignIn`, habilita registro con correo y atributos básicos, y agrega `Pedidos360-Frontend`. Crea un usuario de prueba.

## 5. Código

En `frontend/src/environments/environment.production.ts`:

```ts
authEnabled: true,
apiUrl: 'https://API_ID.execute-api.REGION.amazonaws.com/api',
msal: {
  clientId: '<FRONTEND_CLIENT_ID>',
  authority: 'https://<SUBDOMINIO>.ciamlogin.com/',
  knownAuthorities: ['<SUBDOMINIO>.ciamlogin.com'],
  redirectUri: 'https://<URL_FRONTEND>/',
  apiScope: 'api://<API_CLIENT_ID>/access_as_user'
}
```

En EC2:

```text
AUTH_ENABLED=true
OAUTH2_ISSUER_URI=<CLAIM iss EXACTO DEL ACCESS TOKEN>
OAUTH2_AUDIENCE=<API_CLIENT_ID>
CORS_ALLOWED_ORIGINS=https://<URL_FRONTEND>
```

En External ID el issuer normalmente usa `ciamlogin.com`; comprueba siempre `iss`, `aud` y `scp` del access token y el documento `/.well-known/openid-configuration`.

## Evidencias

- Tenant, usuarios y flujo de usuario.
- Dos aplicaciones registradas.
- Redirect URI y scope.
- Login desde Angular.
- Claims `iss`, `aud`, `scp` y expiración, ocultando el token completo.
