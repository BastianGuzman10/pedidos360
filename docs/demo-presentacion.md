# Demostración de 5 a 10 minutos

1. **Problema (30 s):** Pedidos360 centraliza productos y pedidos.
2. **Arquitectura (45 s):** muestra `arquitectura.md`.
3. **Tenant (60 s):** tenant, usuarios, dos registros y flujo de usuario.
4. **Login (60 s):** inicia sesión y muestra el usuario autenticado.
5. **API Gateway (90 s):** integración, rutas, CORS y authorizer JWT.
6. **Pruebas (2 min):** health `200`, sin token `401`, inválido `401`, sin scope `403`, válido `200`.
7. **CRUD (1 min):** crea producto y pedido; muestra el JSON.
8. **Cierre (30 s):** explica PKCE, doble validación JWT y variables de entorno.

Frase final: “Entra ID emite el token, API Gateway controla el acceso y Spring Boot vuelve a validar issuer, audience y scope antes de ejecutar cada operación.”
