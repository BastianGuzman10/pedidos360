# Checklist contra la rúbrica EP2

| Indicador | Implementación | Evidencia que falta capturar |
|---|---|---|
| Rutas API Manager | Tabla completa en `api-gateway.md` y controladores REST | Pantalla Routes e integración de API Gateway |
| CORS | Orígenes, métodos y headers mínimos en backend y guía | Pantalla CORS + llamada desde Angular |
| Tenant | Guía External ID | Overview del tenant, dominio y usuarios |
| Aplicación | SPA + API, redirect URI y scope documentados | Pantallas App registrations / Expose an API |
| Flujo de usuario | Sign-up/sign-in documentado | Pantalla User flows y registro real |
| Authorization Code + PKCE | MSAL Angular sin Client Secret | Login y Network mostrando `code`, no token en URL |
| Validación JWT | API Gateway authorizer + Spring Resource Server | `200`, `401`, `403`; claims `iss`, `aud`, `scp` |
| Funcionamiento de rutas | CRUD productos/pedidos y health | JSON de cada ruta con token válido |

## Antes de presentar

- [ ] Sustituir todos los `PLACEHOLDER` de producción.
- [ ] Confirmar que `AUTH_ENABLED=true` en EC2.
- [ ] Confirmar HTTPS en frontend y API Gateway.
- [ ] Verificar `/api/health` público.
- [ ] Verificar rutas protegidas sin token (`401`).
- [ ] Verificar token válido sin scope (`403`).
- [ ] Verificar token válido con `access_as_user` (`200`).
- [ ] Probar preflight CORS desde la URL final.
- [ ] Ocultar tokens, contraseñas y claves en capturas.
- [ ] Apagar recursos cobrables después de la evaluación.
