# Publicacion en GitHub

Revisa `git status` y confirma que no existan `.env`, tokens, contrasenas, certificados, `node_modules` ni `target`. Crea un repositorio privado o publico segun la politica academica y ejecuta:

```powershell
git add .
git commit -m "feat: initial Pedidos360 platform"
git branch -M main
git remote add origin URL_DEL_REPOSITORIO
git push -u origin main
```

Configura branch protection y secret scanning en GitHub. Los valores de Entra ID de los archivos environment son placeholders; no subas secretos ni Client Secrets.
