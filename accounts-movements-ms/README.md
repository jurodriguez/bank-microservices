
---

# ✅ README – Microservicio **accounts-movements-ms**

# Accounts Movements Microservice

Microservicio encargado de manejar:

- Creación de cuentas
- Registro de movimientos (depósitos / retiros)
- Generación de balance actualizado
- Sincronización con información de clientes vía RabbitMQ
- Exposición de reportes consolidados

---

## 📌 Tecnologías

- Java 21 
- Spring Boot 3  
- Spring WebFlux  
- R2DBC + MySQL  
- RabbitMQ  
- Testcontainers  
- Docker / Docker Compose  

---

---

## ▶️ Endpoints principales

| Método | Endpoint     | Descripción |
|-------|--------------|-------------|
| POST  | `/cuentas` | Crea una cuenta |
| GET   | `/cuentas/{id}` | Obtiene información |
| POST  | `/movements` | Registra un movimiento |
| GET   | `/report/{accountNumber}` | Reporte completo (cliente + cuenta + movimientos) |

---

## 🐇 Eventos RabbitMQ

Este micro recibe:

- Exchange: **customer.events.exchange**
- Queue: **customer.accounts.queue**
- RoutingKey: **customer.created**

Sirve para sincronizar el cliente con las cuentas.

---

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

Se implementó una prueba de integración usando:

- Testcontainers (MySQL)
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
