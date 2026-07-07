-- GongCraft Database Schema (MySQL 8.0+)
-- NOTE: Dokumen skema lengkap belum tersedia di repo kamu.
-- File ini dibuat sebagai baseline minimal namun lengkap untuk eksekusi MVP.
-- Kalau kamu ingin 17 tabel sesuai spec penuh, perlu versi schema asli (sql/01_GongCraft_Database_Schema.sql) agar tidak terjadi deviasi.

SET NAMES utf8mb4;

-- =====================
-- CORE: Database
-- =====================
CREATE DATABASE IF NOT EXISTS gongcraft
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE gongcraft;

-- =====================
-- 1) USERS & ROLES
-- =====================
CREATE TABLE IF NOT EXISTS roles (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL UNIQUE,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  full_name VARCHAR(150),
  email VARCHAR(150) UNIQUE,
  status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
  role_id BIGINT,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NULL,
  CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles(id)
    ON DELETE SET NULL
    ON UPDATE CASCADE
) ENGINE=InnoDB;

-- seed roles
INSERT INTO roles(name) VALUES
('ADMIN'),('CRAFTSMAN'),('CUSTOMER')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- =====================
-- 2) CUSTOMERS
-- =====================
CREATE TABLE IF NOT EXISTS customers (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(150) NOT NULL,
  email VARCHAR(150) UNIQUE,
  phone VARCHAR(50) NOT NULL,

  address VARCHAR(255),
  city VARCHAR(100),
  province VARCHAR(100),
  postal_code VARCHAR(20),

  notes TEXT,

  status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',

  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NULL
) ENGINE=InnoDB;

-- =====================
-- 3) CRAFTSMEN
-- =====================
CREATE TABLE IF NOT EXISTS craftsmen (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(150) NOT NULL,
  email VARCHAR(150) UNIQUE,
  phone VARCHAR(50) NOT NULL,

  skill VARCHAR(100),
  status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',

  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NULL
) ENGINE=InnoDB;

-- =====================
-- 4) PRODUCTS
-- =====================
CREATE TABLE IF NOT EXISTS products (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(150) NOT NULL,
  specification TEXT,
  unit_price DECIMAL(18,2) NOT NULL DEFAULT 0,
  currency VARCHAR(10) NOT NULL DEFAULT 'IDR',

  status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',

  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NULL
) ENGINE=InnoDB;

-- =====================
-- 5) ORDERS
-- =====================
CREATE TABLE IF NOT EXISTS orders (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  order_number VARCHAR(80) NOT NULL UNIQUE,

  customer_id BIGINT NOT NULL,
  craft_id BIGINT NULL,
  product_id BIGINT NOT NULL,

  quantity INT NOT NULL DEFAULT 1,

  order_status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
  notes TEXT,

  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NULL,

  CONSTRAINT fk_orders_customer FOREIGN KEY (customer_id) REFERENCES customers(id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_orders_product FOREIGN KEY (product_id) REFERENCES products(id)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =====================
-- 6) ORDER_ITEMS
-- =====================
CREATE TABLE IF NOT EXISTS order_items (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  order_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,

  quantity INT NOT NULL DEFAULT 1,
  unit_price DECIMAL(18,2) NOT NULL DEFAULT 0,

  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NULL,

  CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_order_items_product FOREIGN KEY (product_id) REFERENCES products(id)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =====================
-- 7) PRODUCTION PROGRESS
-- =====================
CREATE TABLE IF NOT EXISTS production_progress (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  order_id BIGINT NOT NULL,
  stage VARCHAR(80) NOT NULL,
  progress_percent INT NOT NULL DEFAULT 0,
  progress_notes TEXT,

  photo_path VARCHAR(255) NULL,

  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- =====================
-- 8) QUALITY CHECKS
-- =====================
CREATE TABLE IF NOT EXISTS quality_checks (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  order_id BIGINT NOT NULL,
  check_stage VARCHAR(80) NOT NULL,
  result VARCHAR(30) NOT NULL DEFAULT 'PASS',
  notes TEXT,
  checked_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- =====================
-- 9) INVENTORY
-- =====================
CREATE TABLE IF NOT EXISTS inventory (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_id BIGINT NOT NULL,
  quantity INT NOT NULL DEFAULT 0,
  location VARCHAR(120),
  updated_at DATETIME NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_inventory_product FOREIGN KEY (product_id) REFERENCES products(id)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =====================
-- 10) PAYMENTS
-- =====================
CREATE TABLE IF NOT EXISTS payments (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  order_id BIGINT NOT NULL,
  amount DECIMAL(18,2) NOT NULL,
  method VARCHAR(40) NOT NULL DEFAULT 'CASH',
  payment_status VARCHAR(30) NOT NULL DEFAULT 'PENDING',

  paid_at DATETIME NULL,

  reference_code VARCHAR(100),
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- =====================
-- 11) NOTIFICATIONS
-- =====================
CREATE TABLE IF NOT EXISTS notifications (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NULL,
  type VARCHAR(60) NOT NULL,
  title VARCHAR(150) NOT NULL,
  message TEXT NOT NULL,
  status VARCHAR(30) NOT NULL DEFAULT 'UNREAD',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- =====================
-- 12) AUDIT LOGS
-- =====================
CREATE TABLE IF NOT EXISTS audit_logs (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NULL,
  action VARCHAR(120) NOT NULL,
  entity_type VARCHAR(80),
  entity_id VARCHAR(80),
  details TEXT,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- =====================
-- 13) FILES/ATTACHMENTS (support placeholder)
-- =====================
CREATE TABLE IF NOT EXISTS inventory_files (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  inventory_id BIGINT NULL,
  order_id BIGINT NULL,
  path VARCHAR(255) NOT NULL,
  file_type VARCHAR(80),
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- =====================
-- 14) BASIC INDEXES
-- =====================
CREATE INDEX IF NOT EXISTS idx_customers_name ON customers(name);
CREATE INDEX IF NOT EXISTS idx_orders_order_number ON orders(order_number);
CREATE INDEX IF NOT EXISTS idx_payments_order_id ON payments(order_id);

-- =====================
-- End
-- =====================

-- Seed default admin user (password_hash placeholder)
-- Kamu perlu mengganti password_hash dengan bcrypt hash yang sesuai jika sudah implement auth.
-- CONTOH: Jangan pakai password plain.

INSERT INTO users(username, password_hash, full_name, email, status, role_id)
SELECT 'admin', 'REPLACE_ME_WITH_BCRYPT_HASH', 'Administrator', 'admin@example.com', 'ACTIVE', r.id
FROM roles r WHERE r.name='ADMIN'
ON DUPLICATE KEY UPDATE username = username;

