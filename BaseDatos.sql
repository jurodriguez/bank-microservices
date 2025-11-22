CREATE DATABASE IF NOT EXISTS bank_db;
USE bank_db;


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

CREATE TABLE accounts (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  account_number VARCHAR(30) NOT NULL UNIQUE,
  customer_id BIGINT NOT NULL,
  account_type VARCHAR(50) NOT NULL,
  balance DECIMAL(15,2) NOT NULL DEFAULT 0,
  status VARCHAR(20) NOT NULL,
  
  CONSTRAINT fk_account_customer
    FOREIGN KEY (customer_id) REFERENCES customers(id)
      ON DELETE CASCADE
      ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS movements (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  account_id BIGINT NOT NULL,
  movement_type VARCHAR(50) NOT NULL,
  amount DECIMAL(15,2) NOT NULL,
  balance_after DECIMAL(15,2) NOT NULL,
  description VARCHAR(255),
  movement_date DATETIME NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  
  CONSTRAINT fk_movement_account
    FOREIGN KEY (account_id) REFERENCES accounts(id)
      ON DELETE CASCADE ON UPDATE CASCADE
);
