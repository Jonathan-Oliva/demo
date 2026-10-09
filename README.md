# TP2 · Persistencia, Migraciones y Arquitectura Hexagonal

Este proyecto es la resolución del Trabajo Práctico N°2. Evoluciona la API REST del TP1 integrando persistencia real con PostgreSQL mediante JPA/Hibernate y Flyway, aplicando el patrón de Arquitectura Hexagonal (Puertos y Adaptadores).

## Cómo levantar el proyecto y la Base de Datos

El proyecto ahora depende de una base de datos PostgreSQL. 

1. **Levantar PostgreSQL (vía Docker):**
   Abre una terminal en la raíz del proyecto y ejecuta:
   ```bash
   docker compose up -d
   ```
   Esto levantará un contenedor con PostgreSQL 17 configurado con la base `webii_tp2` y las credenciales necesarias.

2. **Ejecutar la API:**
   Una vez que la base de datos esté lista, ejecuta el proyecto de Spring Boot (en la terminal o desde tu IDE):
   ```bash
   ./mvnw spring-boot:run
   ```
   Al iniciar, Flyway aplicará automáticamente las migraciones SQL en la base de datos para crear y actualizar las tablas.

## ¿Qué cambió respecto al TP1 en la capa de persistencia?

Se reemplazó el almacenamiento en memoria (`Map`) por una base de datos relacional (PostgreSQL) aplicando rigurosamente el patrón de **Puertos y Adaptadores**.

- El dominio (`Favorito`, `Lista`) y los **Puertos** (`FavoritoRepository`, `ListaRepository`) se mantuvieron puros y sin anotaciones de bases de datos.
- El **Servicio** y el **Controlador** nunca se enteraron de que cambiamos el motor de base de datos, ya que siguen dependiendo estrictamente de la interfaz (puerto) y no de la implementación.
- Creamos las entidades JPA (`FavoritoEntity`, `ListaEntity`) y repositorios de Spring Data (`JpaRepository`).
- Se construyeron los **Adaptadores** (`FavoritoRepositoryAdapter`, `ListaRepositoryAdapter`) que implementan las interfaces del puerto. Ellos son la barrera que se encarga de traducir los objetos del dominio a entidades JPA para guardarlos, y viceversa al consultarlos.

## Justificaciones

### 1. Evolución del esquema
**¿Por qué se resolvió el cambio de `lista_id` con una migración nueva (`V4`) en lugar de editar las migraciones `V1` o `V3` ya aplicadas?**
Flyway es sumamente estricto con la integridad del esquema. Cuando aplica una migración, guarda un "checksum" (una firma digital) en su tabla interna `flyway_schema_history`. Si editáramos la `V1` o `V3` ya aplicadas, el checksum cambiaría y Flyway rechazaría arrancar el proyecto para advertirnos que el código fuente ya no coincide con lo que está corriendo en la base de datos real. Las migraciones aplicadas nunca se tocan. Cualquier evolución (como volver una columna `NOT NULL` sin romper los datos) debe hacerse agregando un archivo nuevo (`V4`) que haga un tratamiento seguro de los datos preexistentes.

### 2. Transacciones (ACID)
**¿Por qué la operación de mover favoritos tiene `@Transactional`?**
El método `moverFavoritos` realiza un proceso de negocio de múltiples pasos: primero reasigna un grupo de favoritos a otra lista destino (varios `UPDATE` encubiertos en `save()`), y al finalizar elimina la lista original (`DELETE`). Si sacáramos el `@Transactional` y ocurriera un fallo inesperado (ej. la red se corta o la base de datos rechaza la eliminación de la lista origen por alguna restricción), quedarían los favoritos reasignados, pero la lista de origen seguiría existiendo, rompiendo por completo la consistencia de los datos del sistema. La anotación `@Transactional` garantiza la regla de **Atomicidad** (la 'A' de ACID): o se ejecutan ambos pasos de forma exitosa y se hace un commit, o se deshace todo automáticamente (rollback) dejando la base exactamente como estaba.

## Documentación Interactiva (Swagger)

La API cuenta con documentación autogenerada:
**[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

Ahí podrás visualizar los módulos de **Productos**, **Favoritos** y **Listas**, probando directamente los diferentes endpoints.

