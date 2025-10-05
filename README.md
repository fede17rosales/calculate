# Challenge API

API de ejemplo para cálculos y registro de historial de llamadas.

---

## Requisitos

* Docker y Docker Compose
* Java 17+
* Maven

---

## Levantar la aplicación con Docker

Archivo `docker-compose.yml` ejemplo:

```yaml
version: '3.8'

services:
  db:
    image: postgres:15
    environment:
      POSTGRES_DB: test
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - '5432:5432'

  java_app:
    build: .
    ports:
      - '8080:8080'
    depends_on:
      - db
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/test
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: postgres
```

Para levantar los servicios:

```bash
docker-compose up --build
```

Esto iniciará PostgreSQL y tu aplicación Spring Boot, creando automáticamente las tablas necesarias.

---

## Endpoints

### 1️⃣ Calcular

Realiza un cálculo con dos números y devuelve el resultado, porcentaje y valor final.

**Request:**

```bash
curl --location 'http://localhost:8080/calculate?num1=20&num2=1'
```

**Response:**

```json
{
    "sum": 2.0,
    "percentage": 12.5,
    "finalValue": 2.25
}
```

---

### 2️⃣ Historial

Obtiene el historial de llamadas realizadas a la API.

**Request:**

```bash
curl --location 'http://localhost:8080/history?page=0'
```

**Response:**

```json
{
    "content": [
        {
            "timestamp": "2025-10-05T23:08:31.216372Z",
            "endpoint": "/calculate",
            "params": "1.0,1.0",
            "response": "{ sum: 2.0000, percentage: 12.5000, finalValue: 2.2500 }",
            "error": null
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 10,
        "sort": {
            "sorted": false,
            "empty": true,
            "unsorted": true
        },
        "offset": 0,
        "paged": true,
        "unpaged": false
    },
    "totalElements": 1,
    "totalPages": 1,
    "last": true,
    "size": 10,
    "number": 0,
    "sort": {
        "sorted": false,
        "empty": true,
        "unsorted": true
    },
    "numberOfElements": 1,
    "first": true,
    "empty": false
}
```

---

## Swagger UI

La documentación interactiva de la API está disponible en:

[http://localhost:8081/swagger-ui/index.html#/](http://localhost:8081/swagger-ui/index.html#/)

---

## Notas

* La aplicación está configurada para crear automáticamente las tablas en PostgreSQL al iniciar.
* Para probar los endpoints podés usar `curl`, Postman o Swagger UI.
* Cambiá los puertos según tu configuración de `application.yml` si es necesario.
