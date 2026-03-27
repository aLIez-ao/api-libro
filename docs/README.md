# API Libro

API REST desarrollada con **Spring Boot** para la gestión de libros, implementando operaciones CRUD completas sobre una base de datos PostgreSQL.

---

## Tecnologías utilizadas

| Tecnología           | Versión     |
|----------------------|-------------|
| Java                 | 17          |
| Spring Boot          | 3.x         |
| Spring Data JPA      | -           |
| Spring Web           | -           |
| Spring Validation    | -           |
| Lombok               | -           |
| PostgreSQL Driver    | -           |
| Maven                | -           |

---

## Estructura del proyecto

```
src/main/java/com/ao/api_libro/
│
├── ApiLibroApplication.java
│
├── model/
│   └── Libro.java
│
├── repository/
│   └── LibroRepository.java
│
├── dto/
│   ├── LibroRequestDTO.java
│   └── LibroResponseDTO.java
│
├── service/
│   ├── LibroService.java
│   └── LibroServiceImpl.java
│
├── controller/
│   └── LibroController.java
│
└── exception/
    ├── GlobalExceptionHandler.java
    ├── LibroNotFoundException.java
    └── ApiError.java
```

---

## Configuración

### Base de datos

| Parámetro  | Valor        |
|------------|--------------|
| Motor      | PostgreSQL   |
| Puerto     | 3308         |
| Usuario    | postgres     |
| Contraseña | 1212         |
| Base datos | api_libro    |

### application.properties

```properties
spring.application.name=api-libro

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:3308/api_libro
spring.datasource.username=postgres
spring.datasource.password=1212
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

---

## 🗃Modelo de datos — Tabla `libro`

| Columna              | Tipo          | Restricciones              |
|----------------------|---------------|----------------------------|
| `id`                 | BIGINT        | PK, autoincremental        |
| `titulo`             | VARCHAR(255)  | NOT NULL                   |
| `autor`              | VARCHAR(255)  | NOT NULL                   |
| `editorial`          | VARCHAR(255)  | NOT NULL                   |
| `correo_editorial`   | VARCHAR(255)  | NOT NULL, formato email    |
| `precio`             | DECIMAL       | NOT NULL, mayor que 0      |
| `genero`             | VARCHAR(100)  | NOT NULL                   |

---

## Endpoints disponibles

Base URL: `http://localhost:8080/api/libros`

| Método   | Endpoint          | Descripción                        |
|----------|-------------------|------------------------------------|
| `GET`    | `/`               | Obtener todos los libros           |
| `GET`    | `/{id}`           | Obtener un libro por ID            |
| `POST`   | `/`               | Crear un nuevo libro               |
| `PUT`    | `/{id}`           | Actualizar un libro completo       |
| `PATCH`  | `/{id}`           | Actualizar parcialmente un libro   |
| `DELETE` | `/{id}`           | Eliminar un libro por ID           |

---

## Ejemplos de Request / Response

### POST `/api/libros` — Crear libro

**Request Body:**
```json
{
  "titulo": "El Quijote",
  "autor": "Miguel de Cervantes",
  "editorial": "Editorial Planeta",
  "correoEditorial": "contacto@planeta.com",
  "precio": 29.99,
  "genero": "Novela"
}
```

**Response `201 Created`:**
```json
{
  "id": 1,
  "titulo": "El Quijote",
  "autor": "Miguel de Cervantes",
  "editorial": "Editorial Planeta",
  "correoEditorial": "contacto@planeta.com",
  "precio": 29.99,
  "genero": "Novela"
}
```

---

### GET `/api/libros` — Obtener todos

**Response `200 OK`:**
```json
[
  {
    "id": 1,
    "titulo": "El Quijote",
    "autor": "Miguel de Cervantes",
    "editorial": "Editorial Planeta",
    "correoEditorial": "contacto@planeta.com",
    "precio": 29.99,
    "genero": "Novela"
  }
]
```

---

### GET `/api/libros/{id}` — Obtener por ID

**Response `200 OK`** → objeto libro  
**Response `404 Not Found`:**
```json
{
  "status": 404,
  "error": "Not Found",
  "mensaje": "Libro con id 99 no encontrado",
  "timestamp": "2025-03-26T10:00:00"
}
```

---

### PUT `/api/libros/{id}` — Actualización completa

**Request Body:** todos los campos obligatorios  
**Response `200 OK`** → objeto libro actualizado

---

### PATCH `/api/libros/{id}` — Actualización parcial

**Request Body:** solo los campos a modificar
```json
{
  "precio": 19.99
}
```
**Response `200 OK`** → objeto libro actualizado

---

### DELETE `/api/libros/{id}` — Eliminar

**Response `204 No Content`**  
**Response `404 Not Found`** → si el libro no existe

---

## ⚠️ Manejo de excepciones

Las excepciones se manejan de forma centralizada mediante `@RestControllerAdvice`.

| Excepción                        | HTTP Status           |
|----------------------------------|-----------------------|
| `LibroNotFoundException`         | `404 Not Found`       |
| `MethodArgumentNotValidException`| `400 Bad Request`     |
| `Exception` (genérica)           | `500 Internal Server Error` |

---

## Validaciones aplicadas

| Campo              | Validación                          |
|--------------------|-------------------------------------|
| `titulo`           | `@NotBlank`                         |
| `autor`            | `@NotBlank`                         |
| `editorial`        | `@NotBlank`                         |
| `correoEditorial`  | `@NotBlank`, `@Email`               |
| `precio`           | `@NotNull`, `@Positive`             |
| `genero`           | `@NotBlank`                         |

---

## Cómo ejecutar el proyecto

1. Clonar el repositorio
2. Crear la base de datos en PostgreSQL:
   ```sql
   CREATE DATABASE api_libro;
   ```
3. Verificar la configuración en `application.properties`
4. Ejecutar con Maven:
   ```bash
   ./mvnw spring-boot:run
   ```
5. Acceder a: `http://localhost:8080/api/libros`

---
