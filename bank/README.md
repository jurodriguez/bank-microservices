# Customer Persona Microservice

Microservicio encargado de gestionar la información de clientes (persona).  
Provee endpoints CRUD y publica eventos al ecosistema para sincronización con otros servicios, como `accounts-movements-ms`.

---

## 📌 Tecnologías

- Java 21
- Spring Boot 3
- WebFlux
- MySQL (R2DBC)
- RabbitMQ
- Testcontainers
- Docker / Docker Compose


---

## ▶️ Endpoints principales

| Método | Endpoint     | Descripción                       |
|-------|--------------|-----------------------------------|
| POST  | `/customers` | Crea un cliente                   |
| GET   | `/customers/{id}` | Obtiene info del cliente          |
| PUT   | `/customers/{id}` | Actualiza info                    |
| DELETE| `/customers/{id}` | Elimina                           |
| GET   | `/customers` | Obtiene info de todos los cliente |
---

## 🐇 Eventos RabbitMQ

Este microservicio publica eventos cuando un cliente es creado o actualizado:

- Exchange: **customer.events.exchange**
- Queue consumida por accounts: **customer.accounts.queue**
- RoutingKey: **customer.created**

## 🗄 Variables de Entorno

DB_URL=r2dbc:pool:mysql://localhost:3306/bank_db

DB_USERNAME=root

DB_PASSWORD=root

RABBIT_HOST=localhost

RABBIT_PORT=5672

RABBIT_USERNAME=admin

RABBIT_PASSWORD=admin


---

## 🧪 Pruebas

Incluye mínimo una prueba de integración usando:

- Testcontainers MySQL
- WebTestClient

---

## 🐳 Despliegue con Docker

### 1️⃣ Construir la imagen

./gradlew bootJar

---

## 📦 `Dockerfile`

```dockerfile
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## Ejecución Local

./gradlew bootRun
