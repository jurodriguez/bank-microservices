CREATE TABLE customers (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,

    name VARCHAR(100) NOT NULL,
    gender VARCHAR(20),
    age INT,
    identification VARCHAR(50) NOT NULL UNIQUE,
    address VARCHAR(200),
    phone VARCHAR(50),

    password VARCHAR(255) NOT NULL,
    status BOOLEAN NOT NULL
);