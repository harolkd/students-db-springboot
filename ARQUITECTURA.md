# Arquitectura de la API de Gestión de Estudiantes

## 1. Resumen

API REST que permite registrar y consultar estudiantes de una institución educativa y se compone de dos servicios: la **API** y una **base de datos**.

**Stack sugerido** (intercambiable, ya que el diseño es agnóstico):

| Capa | Tecnología sugerida |
|---|---|
| Lenguaje / framework | Python + FastAPI (o Node.js + Express) |
| Base de datos | PostgreSQL |
| Contenedores | Docker + Docker Compose |
| Documentación | OpenAPI/Swagger (generada automáticamente) |

---

## 2. Diseño de la API

**Base URL:** `/api/v1` (versionado desde el inicio para evolucionar sin romper clientes)
**Formato:** JSON (`application/json`)

### 2.1 Endpoints

| Método | Ruta | Descripción | Éxito | Errores posibles |
|---|---|---|---|---|
| POST | `/students` | Crear un estudiante | `201 Created` | `400` datos inválidos, `409` correo duplicado, `422` validación |
| GET | `/students/{id}` | Obtener un estudiante por ID | `200 OK` | `404` no encontrado, `400` ID con formato inválido |
| GET | `/students` | Listar todos los estudiantes | `200 OK` | `500` error interno |
| GET | `/health` | Verificar estado del servicio | `200 OK` | `503` si la BD no responde |

> `/health` no es una operación de negocio, pero es necesario para que el orquestador de contenedores sepa si la API está operativa.

### 2.2 Contratos

**POST /students**
- *Entrada:* `nombre`, `apellido`, `correo_electronico` (todos obligatorios).
- *Respuesta:* el estudiante creado, incluyendo `id` y `fecha_creacion`. Incluye el encabezado `Location` con la URL del nuevo recurso.

**GET /students/{id}**
- *Entrada:* `id` en la ruta.
- *Respuesta:* `id`, `nombre`, `apellido`, `correo_electronico`, `fecha_creacion`.

**GET /students**
- *Respuesta:* lista de estudiantes. Se recomienda soportar paginación opcional mediante `limit` y `offset`, con valores por defecto razonables para evitar respuestas gigantes.

### 2.3 Reglas de validación

| Campo | Regla |
|---|---|
| `nombre` | Obligatorio, texto, 1–100 caracteres, sin espacios solo |
| `apellido` | Obligatorio, texto, 1–100 caracteres |
| `correo_electronico` | Obligatorio, formato de correo válido, **único**, normalizado a minúsculas |
| `id` | Generado por el servidor; el cliente nunca lo envía |

### 2.4 Formato estándar de errores

Todas las respuestas de error comparten estructura: un código HTTP, un identificador corto del error (por ejemplo `STUDENT_NOT_FOUND`), un mensaje legible y, en validaciones, el detalle por campo. Esto facilita el manejo uniforme por parte de los clientes.

---

## 3. Modelo de datos

**Entidad: Estudiante**

| Atributo | Tipo | Restricciones |
|---|---|---|
| `id` | UUID | Clave primaria, generado por el servidor |
| `nombre` | Texto (100) | No nulo |
| `apellido` | Texto (100) | No nulo |
| `correo_electronico` | Texto (255) | No nulo, **índice único** |
| `fecha_creacion` | Timestamp UTC | No nulo, valor por defecto = momento de inserción |

**Decisión:** se usa UUID en lugar de entero autoincremental para que los identificadores no sean predecibles ni enumerables, y para facilitar una futura integración con otros sistemas.

---

## 4. Arquitectura lógica (capas)

```
Cliente (Postman, front-end, otros sistemas)
        │  HTTP/JSON
        ▼
┌─────────────────────────────────────────┐
│  CONTENEDOR: api                        │
│                                         │
│  Capa de Presentación (Controladores)   │
│   · Rutas, validación de entrada,       │
│     códigos HTTP, serialización         │
│                 │                       │
│  Capa de Servicio (Lógica de negocio)   │
│   · Reglas: correo único, normalización │
│                 │                       │
│  Capa de Acceso a Datos (Repositorio)   │
│   · Consultas y persistencia            │
└─────────────────┼───────────────────────┘
                  │  Red interna Docker
                  ▼
┌─────────────────────────────────────────┐
│  CONTENEDOR: db (PostgreSQL)            │
│   · Volumen persistente                 │
└─────────────────────────────────────────┘
```

**Responsabilidades**

- **Controladores:** solo traducen HTTP a llamadas internas y viceversa. No contienen reglas de negocio.
- **Servicio:** concentra las reglas (unicidad del correo, normalización). Es la capa más fácil de probar de forma aislada.
- **Repositorio:** única capa que conoce la base de datos. Cambiar de motor de BD solo afecta a esta capa.