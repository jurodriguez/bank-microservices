## 🐳 Despliegue con Docker

### 1️⃣ Levantar todo con docker-compose

docker-compose docker-compose.yml up -d

Esto inicializará:

- Base de datos MySQL
- RabbitMQ
- Microservicios del dominio

### 2️⃣ Ejecutar script para crear base de datos si es necesario y las tablas

create-bank-db.sql