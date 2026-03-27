# 📦 Documentación de Paquetes — API Libro

Este documento describe la arquitectura por capas del proyecto, el rol de cada paquete y los archivos que lo componen.

---

## 🏗️ Arquitectura general

El proyecto sigue una arquitectura en capas (**Layered Architecture**), patrón estándar en aplicaciones Spring Boot:

```
┌─────────────────────────────────┐
│         CONTROLLER              │  ← Recibe peticiones HTTP
├─────────────────────────────────┤
│           SERVICE               │  ← Lógica de negocio
├─────────────────────────────────┤
│         REPOSITORY              │  ← Acceso a base de datos
├─────────────────────────────────┤
│           MODEL                 │  ← Entidad / tabla en BD
└─────────────────────────────────┘
         ▲              ▲
        DTO          EXCEPTION
  (entrada/salida)  (errores centralizados)
```

Cada capa solo se comunica con la capa inmediatamente inferior. Ninguna capa salta niveles.

---

## 📂 Árbol completo de paquetes y archivos

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

## 🔍 Descripción detallada por paquete

---

### 📌 `ApiLibroApplication.java` — Raíz

| Atributo     | Detalle                                      |
|--------------|----------------------------------------------|
| Ubicación    | `com.ao.api_libro`                           |
| Anotación    | `@SpringBootApplication`                     |
| Rol          | Punto de entrada de la aplicación Spring Boot |

Es la clase principal que arranca el contexto de Spring. No debe modificarse salvo configuraciones globales muy específicas.

```
ApiLibroApplication
    └── main(String[] args) → SpringApplication.run(...)
```

---

### 📌 Paquete `model`

| Atributo     | Detalle                                           |
|--------------|---------------------------------------------------|
| Ubicación    | `com.ao.api_libro.model`                          |
| Anotaciones  | `@Entity`, `@Table`, `@Id`, `@GeneratedValue`     |
| Rol          | Representar la tabla `libro` de la base de datos  |

Contiene las clases que mapean directamente a tablas en PostgreSQL mediante JPA/Hibernate. Aquí se definen también las restricciones a nivel de base de datos (`@Column`, `nullable`, `unique`, `length`).

#### Archivos

| Archivo      | Descripción                                    |
|--------------|------------------------------------------------|
| `Libro.java` | Entidad JPA que mapea la tabla `libro` en BD   |

#### Responsabilidades
- Definir los campos y sus tipos de datos
- Aplicar restricciones de columna (`nullable = false`, `length`, `unique`)
- Manejar la generación automática del ID
- Usar Lombok (`@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`) para reducir boilerplate

---

### 📌 Paquete `repository`

| Atributo     | Detalle                                          |
|--------------|--------------------------------------------------|
| Ubicación    | `com.ao.api_libro.repository`                    |
| Anotaciones  | `@Repository` (implícita via `JpaRepository`)    |
| Rol          | Capa de acceso a datos (Data Access Layer)       |

Contiene las interfaces que extienden `JpaRepository`. Spring Data JPA genera automáticamente la implementación en tiempo de ejecución, proveyendo los métodos estándar de CRUD sin escribir SQL.

#### Archivos

| Archivo                  | Descripción                                            |
|--------------------------|--------------------------------------------------------|
| `LibroRepository.java`   | Interfaz que extiende `JpaRepository<Libro, Long>`     |

#### Métodos heredados disponibles (sin escribir código)

| Método                    | Operación SQL equivalente     |
|---------------------------|-------------------------------|
| `findAll()`               | `SELECT * FROM libro`         |
| `findById(id)`            | `SELECT * WHERE id = ?`       |
| `save(libro)`             | `INSERT` o `UPDATE`           |
| `deleteById(id)`          | `DELETE WHERE id = ?`         |
| `existsById(id)`          | `SELECT COUNT(*) WHERE id = ?`|

---

### 📌 Paquete `dto`

| Atributo     | Detalle                                              |
|--------------|------------------------------------------------------|
| Ubicación    | `com.ao.api_libro.dto`                               |
| Anotaciones  | Validaciones de Bean Validation (`@NotBlank`, `@Email`, etc.) |
| Rol          | Transferencia de datos entre cliente y API           |

Los DTOs (**Data Transfer Objects**) desacoplan la entidad interna del modelo de entrada/salida de la API. Esto evita exponer directamente la entidad JPA y permite aplicar validaciones solo en los datos que entran o salen.

#### Archivos

| Archivo                | Descripción                                                                 |
|------------------------|-----------------------------------------------------------------------------|
| `LibroRequestDTO.java` | Datos que el cliente envía al crear o actualizar un libro (con validaciones) |
| `LibroResponseDTO.java`| Datos que la API devuelve al cliente como respuesta                          |

#### ¿Por qué separar Request y Response?

| Razón                          | Explicación                                                             |
|--------------------------------|-------------------------------------------------------------------------|
| Seguridad                      | No exponer campos internos como versiones, auditoría, etc.              |
| Validaciones solo en entrada   | Las restricciones `@Valid` aplican al Request, no al Response           |
| Flexibilidad                   | El Response puede formatear o combinar datos sin afectar la entidad     |
| Principio de responsabilidad   | Cada clase tiene una sola razón de cambio                               |

---

### 📌 Paquete `service`

| Atributo     | Detalle                                               |
|--------------|-------------------------------------------------------|
| Ubicación    | `com.ao.api_libro.service`                            |
| Anotaciones  | `@Service`, `@Transactional`                          |
| Rol          | Capa de lógica de negocio (Business Logic Layer)      |

Es el núcleo de la aplicación. Coordina las operaciones entre el controlador y el repositorio, aplica reglas de negocio y maneja el mapeo entre entidades y DTOs.

#### Archivos

| Archivo                 | Descripción                                                                 |
|-------------------------|-----------------------------------------------------------------------------|
| `LibroService.java`     | Interfaz que declara los métodos disponibles del servicio                   |
| `LibroServiceImpl.java` | Implementación concreta de la interfaz con la lógica de cada operación      |

#### ¿Por qué separar interfaz e implementación?

| Razón                   | Explicación                                                              |
|-------------------------|--------------------------------------------------------------------------|
| Desacoplamiento         | El controlador depende de la abstracción, no de la implementación        |
| Principio de inversión  | Facilita cambiar la implementación sin tocar el controlador              |
| Testabilidad            | Permite crear mocks de la interfaz para pruebas unitarias                |
| Buenas prácticas        | Estándar en proyectos Spring profesionales                               |

#### Métodos del servicio

| Método                              | Operación         |
|-------------------------------------|-------------------|
| `findAll()`                         | Listar todos      |
| `findById(Long id)`                 | Buscar por ID     |
| `save(LibroRequestDTO dto)`         | Crear             |
| `update(Long id, LibroRequestDTO)`  | Actualizar total  |
| `partialUpdate(Long id, Map<>)`     | Actualizar parcial|
| `deleteById(Long id)`               | Eliminar          |

---

### 📌 Paquete `controller`

| Atributo     | Detalle                                              |
|--------------|------------------------------------------------------|
| Ubicación    | `com.ao.api_libro.controller`                        |
| Anotaciones  | `@RestController`, `@RequestMapping`, `@Valid`       |
| Rol          | Capa de presentación — expone los endpoints REST     |

Recibe las peticiones HTTP, delega al servicio y devuelve la respuesta adecuada. No contiene lógica de negocio ni accede directamente al repositorio.

#### Archivos

| Archivo                | Descripción                                         |
|------------------------|-----------------------------------------------------|
| `LibroController.java` | Controlador REST con todos los endpoints de `/api/libros` |

#### Endpoints implementados

| Método HTTP | Ruta              | Método Java          | Respuesta exitosa  |
|-------------|-------------------|----------------------|--------------------|
| `GET`       | `/api/libros`     | `getAll()`           | `200 OK`           |
| `GET`       | `/api/libros/{id}`| `getById()`          | `200 OK`           |
| `POST`      | `/api/libros`     | `create()`           | `201 Created`      |
| `PUT`       | `/api/libros/{id}`| `update()`           | `200 OK`           |
| `PATCH`     | `/api/libros/{id}`| `partialUpdate()`    | `200 OK`           |
| `DELETE`    | `/api/libros/{id}`| `delete()`           | `204 No Content`   |

---

### 📌 Paquete `exception`

| Atributo     | Detalle                                               |
|--------------|-------------------------------------------------------|
| Ubicación    | `com.ao.api_libro.exception`                          |
| Anotaciones  | `@RestControllerAdvice`, `@ExceptionHandler`          |
| Rol          | Manejo centralizado de errores en toda la aplicación  |

Centraliza el manejo de excepciones usando el patrón **Global Exception Handler**. Evita duplicar bloques `try-catch` en controladores y servicio, devolviendo siempre una respuesta JSON estructurada y consistente.

#### Archivos

| Archivo                      | Descripción                                                              |
|------------------------------|--------------------------------------------------------------------------|
| `ApiError.java`              | Modelo estándar de respuesta de error (status, mensaje, timestamp)       |
| `LibroNotFoundException.java`| Excepción personalizada lanzada cuando no se encuentra un libro por ID   |
| `GlobalExceptionHandler.java`| Clase `@RestControllerAdvice` que intercepta y maneja todas las excepciones |

#### Excepciones manejadas

| Excepción                          | Causa                                      | HTTP Status |
|------------------------------------|--------------------------------------------|-------------|
| `LibroNotFoundException`           | Libro no encontrado por ID                 | `404`       |
| `MethodArgumentNotValidException`  | Falló una validación de Bean Validation    | `400`       |
| `HttpMessageNotReadableException`  | JSON malformado en el body                 | `400`       |
| `Exception`                        | Cualquier error no contemplado             | `500`       |

#### Estructura de `ApiError`

```json
{
  "status": 404,
  "error": "Not Found",
  "mensaje": "Libro con id 10 no encontrado",
  "timestamp": "2025-03-26T10:00:00"
}
```

---

## 🔄 Flujo de una petición HTTP

A continuación se ilustra el flujo completo de una petición `POST /api/libros`:

```
Cliente (Postman / Frontend)
        │
        ▼ HTTP POST /api/libros (JSON body)
┌───────────────────┐
│   LibroController │  → Recibe la petición, valida con @Valid
└────────┬──────────┘
         │ Llama a service.save(LibroRequestDTO)
         ▼
┌───────────────────┐
│  LibroServiceImpl │  → Mapea DTO → Entidad, aplica reglas de negocio
└────────┬──────────┘
         │ Llama a repository.save(libro)
         ▼
┌───────────────────┐
│ LibroRepository   │  → Ejecuta INSERT en PostgreSQL
└────────┬──────────┘
         │ Retorna Libro guardado
         ▼
┌───────────────────┐
│  LibroServiceImpl │  → Mapea Entidad → LibroResponseDTO
└────────┬──────────┘
         │
         ▼
┌───────────────────┐
│   LibroController │  → Retorna ResponseEntity<LibroResponseDTO> 201 Created
└───────────────────┘
        │
        ▼ JSON Response
   Cliente recibe respuesta
```

Si ocurre un error en cualquier punto del flujo, el `GlobalExceptionHandler` lo intercepta y devuelve un `ApiError` con el status HTTP correspondiente.

---

## 📎 Dependencias entre paquetes

```
controller  →  service (interfaz)
service     →  repository
service     →  model
service     →  dto
controller  →  dto
exception   →  (intercepta a todos)
repository  →  model
```

> Ningún paquete de nivel inferior conoce a los de nivel superior.
> El `controller` nunca accede directamente al `repository`.