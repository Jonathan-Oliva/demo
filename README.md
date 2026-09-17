# TP1 · Catálogo y Favoritos (Completado)

Este proyecto es la resolución del Trabajo Práctico N°1. Incluye una API REST construida con **Spring Boot** que consume productos de una API externa (DummyJSON) y permite gestionar un catálogo de favoritos (CRUD propio en memoria).

##  Cómo levantar el proyecto

Para correr el proyecto localmente, asegurate de tener Java instalado y ejecutá el siguiente comando en la terminal, desde la raíz del proyecto:

**En Windows:**
```bash
cd demo
.\mvnw.cmd spring-boot:run
```

**En macOS / Linux:**
```bash
cd demo
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

##  Tecnologías Utilizadas
- Spring Boot (WebMVC, Validation)
- Java Records (para inmutabilidad de DTOs)
- RestClient (para consumo de API externa)
- Swagger UI / OpenAPI (para documentación)
- Estructura de Arquitectura Limpia (Controller -> Service -> Repository -> Domain)
