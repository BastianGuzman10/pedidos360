# Despliegue del backend en EC2

1. Crea una instancia EC2 con Amazon Linux 2023, un security group que permita SSH desde tu IP y HTTP/HTTPS desde el balanceador o clientes autorizados. No abras la base de datos a internet.
2. Instala Docker en la instancia y habilita el servicio. Clona el repositorio desde GitHub usando un mecanismo de acceso seguro; nunca guardes claves privadas en el repo.
3. Configura variables en el entorno de la instancia: `OAUTH2_ISSUER_URI`, `OAUTH2_AUDIENCE`, `CORS_ALLOWED_ORIGINS`, `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` y `SPRING_DATASOURCE_PASSWORD`.
4. Usa PostgreSQL administrado o una red privada para la base de datos. No uses las credenciales locales de `docker-compose.yml` en produccion.
5. Construye y ejecuta solo el backend: `docker build -t pedidos360-api ./backend` y `docker run -d --restart unless-stopped --name pedidos360-api -p 8080:8080 --env-file .env pedidos360-api`.
6. Coloca HTTPS delante con un Application Load Balancer y certificado ACM. Apunta API Gateway al endpoint privado/publico definido por tu arquitectura y limita el security group al origen necesario.
7. Configura `/api/health` como health check. Revisa logs y Actuator; no expongas H2 console en produccion.
8. Valida primero en un entorno de prueba y luego promueve con una imagen versionada. El despliegue no se ejecuta automaticamente desde este proyecto.

Para rollback conserva la etiqueta anterior de la imagen. Usa IAM roles, Secrets Manager/SSM Parameter Store y CloudWatch en lugar de secretos en archivos.
