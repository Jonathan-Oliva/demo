# TP1 · Catálogo y Favoritos (Completado)

Este proyecto es la resolución del Trabajo Práctico N°1. Incluye una API REST construida con **Spring Boot** que consume productos de una API externa (DummyJSON) y permite gestionar un catálogo de favoritos (CRUD propio en memoria).

##  Cómo levantar el proyecto

Para correr el proyecto localmente, asegurate de tener Java instalado y ejecutá el siguiente comando en la terminal, desde la raíz del proyecto:

**En Windows:**
```bash
.\mvnw.cmd spring-boot:run
```

**En macOS / Linux:**
```bash
./mvnw spring-boot:run
```

Una vez que veas en la consola el mensaje `Started DemoApplication`, el servidor estará levantado en tu máquina.

##  Documentación 

La API cuenta con documentación interactiva autogenerada gracias a `springdoc-openapi`.
Una vez que el servidor esté corriendo, podés ver todos los endpoints, probarlos y leer sus descripciones entrando a esta URL desde tu navegador:

 **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

##  Endpoints Disponibles

###  Catálogo de Productos (API Externa)
- `GET /api/productos` - Lista todos los productos del catálogo.
- `GET /api/productos/{id}` - Obtiene el detalle de un producto específico.

###  Favoritos (CRUD en Memoria)
- `POST /api/favoritos` - Crea un nuevo favorito (Requiere enviar `idProductoExterno` y `notaPersonal`).
- `GET /api/favoritos` - Lista todos los favoritos guardados.
- `GET /api/favoritos/{id}` - Obtiene el detalle de un favorito específico.
- `PUT /api/favoritos/{id}` - Actualiza la nota o el producto de un favorito.
- `DELETE /api/favoritos/{id}` - Elimina un favorito del sistema.

###  Utilidades
- `GET /health` - Chequeo de estado de la aplicación.
- `GET /ping` - Devuelve un simple `pong`.
- `POST /api/auth/registro` - Registra un usuario con contraseña hasheada; devuelve `409 Conflict` si el email ya existe.
- `POST /api/auth/login` - Valida las credenciales y devuelve un token JWT Bearer válido por una hora.

Para acceder a rutas protegidas (`/api/favoritos/**` y `/api/listas/**`), enviá el token en `Authorization: Bearer <token>`. `/api/auth/**` y `/api/productos/**` son públicos.

##  Tecnologías Utilizadas
- Spring Boot (WebMVC, Validation)
- Spring Security y JWT (JJWT)
- JUnit, Spring Boot Test y Testcontainers (PostgreSQL)
- Java Records (para inmutabilidad de DTOs)
- RestClient (para consumo de API externa)
- Swagger UI / OpenAPI (para documentación)
- Estructura de Arquitectura Limpia (Controller -> Service -> Repository -> Domain)

## Tests

Los tests unitarios terminan en `*Test` y se ejecutan con Surefire:

```bash
./mvnw test
```

Las pruebas de integración terminan en `*IT` y Failsafe las ejecuta durante la fase `verify`:

```bash
./mvnw verify
```

Las pruebas de integración que usan Testcontainers necesitan Docker disponible. `verify` también ejecuta los tests unitarios.

## Pipeline de CI

El pipeline de CI se ejecuta con el mismo comando que valida la compilación y ambos grupos de pruebas:

```bash
./mvnw --batch-mode --no-transfer-progress verify
```

Al configurar el workflow de CI, debe invocar ese comando en cada push y pull request; un resultado exitoso requiere que pasen compilación, tests unitarios y pruebas de integración. El workflow todavía no está agregado al repositorio.

## Configuración local

La aplicación conserva valores por defecto para ejecutarse localmente con la base PostgreSQL del `docker-compose.yml`. Para reemplazarlos, definí estas variables de entorno antes de iniciar la aplicación:

| Variable | Valor por defecto |
| --- | --- |
| `DB_URL` | `jdbc:postgresql://localhost:5432/webii_tp2` |
| `DB_USERNAME` | `webii_tp2` |
| `DB_PASSWORD` | `webii_tp2` |
| `JWT_SECRET` | Clave local de desarrollo incluida en `application.properties` |

En entornos compartidos o desplegados, configurá `JWT_SECRET` con una clave propia y segura, y las credenciales de base de datos mediante variables de entorno.

## Despliegue

Los pasos de despliegue se documentarán más adelante, cuando definamos el entorno y el pipeline de publicación.
