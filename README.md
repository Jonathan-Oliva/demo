# TP3 — Seguridad, pruebas y CI

API REST de productos externos y favoritos, con persistencia PostgreSQL, listas, registro de usuarios y autenticación JWT. La arquitectura separa controladores, servicios, dominio y adaptadores JPA; Flyway administra el esquema.

## Levantar el proyecto

Desde esta carpeta, iniciá PostgreSQL y la aplicación:

```bash
docker compose up -d
./mvnw spring-boot:run
```

En Windows, usá `docker compose up -d` y `.\mvnw.cmd spring-boot:run`. Swagger queda disponible en [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html).

La configuración local tiene valores por defecto. Podés reemplazarlos con `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` y `JWT_SECRET`. Configurá una clave JWT propia y segura fuera del entorno local.

## Persistencia y arquitectura

Favoritos y listas se guardan en PostgreSQL. El dominio y los puertos (`FavoritoRepository`, `ListaRepository`) no dependen de JPA; las entidades y repositorios Spring Data se conectan mediante adaptadores. Flyway administra la evolución del esquema. Las migraciones aplicadas se conservan sin editar: por ejemplo, V4 prepara los datos existentes antes de volver obligatoria la relación entre favoritos y listas.

El traslado de favoritos usa una transacción porque reasigna los favoritos y elimina la lista origen como una única operación: si un paso falla, se revierte el conjunto.

## Endpoints

- `GET /api/productos` y `GET /api/productos/{id}`: catálogo público.
- `POST /api/auth/registro`: registro público; guarda la contraseña hasheada y asigna rol `USER`.
- `POST /api/auth/login`: login público; devuelve un JWT Bearer válido por una hora.
- `/api/favoritos/**`: CRUD de favoritos, requiere JWT.
- `/api/listas/**`: operaciones de listas, requiere JWT.
- `GET /health` y `GET /ping`: utilidades.

En Swagger, usá `POST /api/auth/registro`, luego `POST /api/auth/login`, copiá `token` y pulsá **Authorize**. Pegá el token sin escribir el prefijo `Bearer`; Swagger lo agrega. El rol `ADMIN` puede acceder a todas las listas. El registro público solo crea usuarios `USER`.

La autorización de listas está en `ListaService`: el Controller pasa la identidad autenticada, y el servicio aplica la regla antes de operar. Así la regla de negocio se mantiene aunque otro punto de entrada reutilice el servicio. Las listas que existían antes de incorporar propietarios quedan sin dueño y solo son visibles para `ADMIN`.

## Tests

Los tests unitarios terminan en `*Test` y Surefire los ejecuta con:

```bash
./mvnw test
```

Las pruebas de integración terminan en `*IT` y Failsafe las ejecuta durante `verify`:

```bash
./mvnw verify
```

`ListaServiceImplTest` y `UsuarioServiceImplTest` cubren asociación del dueño y almacenamiento exclusivo del hash. `ListaControllerIT` prueba login y autorización contra PostgreSQL 17 de Testcontainers; Docker debe estar disponible. Testcontainers inicia una base limpia en cada ejecución de integración y Flyway aplica las migraciones desde cero.

## Pipeline de CI

GitHub Actions ejecuta el workflow **CI** en cada `push` y `pull_request`. El job compila y corre tests unitarios y de integración con:

```bash
./mvnw --batch-mode --no-transfer-progress verify
```

Para ver el resultado, abrí la pestaña **Actions** del repositorio y seleccioná la ejecución correspondiente al commit. Una marca verde indica que `verify` terminó correctamente; una cruz roja indica que falló. Abrí el job **Build and test** y el paso de Maven para leer el error: Surefire informa fallos de clases `*Test` y Failsafe los de clases `*IT`. Las pruebas de integración usan Testcontainers y una base PostgreSQL limpia, por lo que CI no depende de una base local.

## Despliegue

El despliegue se configura en Render mediante el Blueprint `render.yaml`. Este crea un servicio web Docker y una base PostgreSQL gratuita en la misma región. El `Dockerfile` compila la aplicación con Java 25; en ejecución, Render entrega host, puerto, nombre y credenciales de PostgreSQL mediante variables de entorno y genera `JWT_SECRET`. Flyway aplica las migraciones al iniciar la aplicación.

Para desplegarlo manualmente:

1. Subí a GitHub los cambios que incluyen `Dockerfile`, `.dockerignore`, `render.yaml` y `application.properties`.
2. En Render, elegí **New → Blueprint**, conectá el repositorio y seleccioná la rama `main`. Dejá el Blueprint Path como `render.yaml` (raíz del repositorio).
3. Revisá los recursos detectados y pulsá **Deploy Blueprint**. Esperá a que terminen la creación de PostgreSQL, la compilación y el deploy del servicio.
4. En los logs del servicio, confirmá que Spring Boot arrancó y que Flyway registró las migraciones aplicadas (`V1` a `V6`).
5. Abrí la URL pública del servicio y comprobá `https://<servicio>.onrender.com/health`; debe responder `{"status":"ok","service":"demo"}`.

La URL PostgreSQL se conecta por la red privada de Render. No pegues credenciales ni el secreto JWT en el repositorio. El plan gratuito de PostgreSQL de Render vence a los 30 días; usalo para una verificación temporal.
