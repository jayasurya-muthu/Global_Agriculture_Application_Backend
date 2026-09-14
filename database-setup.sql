-- ============================================================
--  Global Agriculture — Database setup
-- ============================================================
--  You usually do NOT need to run this file by hand.
--  With spring.jpa.hibernate.ddl-auto=update (already set in
--  application.properties), Spring Boot creates every table below
--  automatically the first time you run the app, and DataSeeder.java
--  inserts the same admin account / categories / products for you.
--
--  Run this file yourself only if you want the database ready
--  BEFORE starting the app, or if you prefer to manage the schema
--  by hand. If you do run it, set:
--      spring.jpa.hibernate.ddl-auto=validate
--  in application.properties afterwards so Hibernate doesn't try to
--  alter what you've already created.
-- ============================================================

CREATE DATABASE IF NOT EXISTS global_agriculture;
USE global_agriculture;

-- ---------- Tables ----------

CREATE TABLE IF NOT EXISTS users (
    user_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name   VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    phone       VARCHAR(50),
    address     VARCHAR(500),
    city        VARCHAR(100),
    state       VARCHAR(100),
    country     VARCHAR(100),
    pincode     VARCHAR(20),
    role        VARCHAR(20) NOT NULL DEFAULT 'BUYER',
    created_at  DATETIME
);

CREATE TABLE IF NOT EXISTS categories (
    category_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_name  VARCHAR(255) NOT NULL UNIQUE,
    description    VARCHAR(1000)
);

CREATE TABLE IF NOT EXISTS products (
    product_id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_name       VARCHAR(255) NOT NULL,
    description        VARCHAR(2000),
    price              DECIMAL(10,2) NOT NULL,
    stock              INT NOT NULL,
    unit               VARCHAR(50),
    image_url          VARCHAR(1000),
    country_of_origin  VARCHAR(100),
    category_id        BIGINT,
    harvest_date       DATE,
    expiry_date        DATE,
    status             VARCHAR(30) NOT NULL DEFAULT 'Available'
);

CREATE TABLE IF NOT EXISTS cart (
    cart_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    buyer_id    BIGINT NOT NULL,
    product_id  BIGINT NOT NULL,
    quantity    INT NOT NULL
);

CREATE TABLE IF NOT EXISTS orders (
    order_id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    buyer_id          BIGINT NOT NULL,
    shipping_address  VARCHAR(1000) NOT NULL,
    order_date        DATETIME,
    total_amount      DECIMAL(10,2) NOT NULL,
    order_status      VARCHAR(30) NOT NULL DEFAULT 'PLACED',
    payment_status    VARCHAR(30) NOT NULL DEFAULT 'PENDING'
);

CREATE TABLE IF NOT EXISTS order_items (
    order_item_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id       BIGINT NOT NULL,
    product_id     BIGINT NOT NULL,
    quantity       INT NOT NULL,
    subtotal       DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS payments (
    payment_id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id         BIGINT NOT NULL,
    payment_method   VARCHAR(30) NOT NULL,
    transaction_id   VARCHAR(255),
    amount           DECIMAL(10,2) NOT NULL,
    payment_status   VARCHAR(30) NOT NULL DEFAULT 'SUCCESS',
    payment_date     DATETIME
);

CREATE TABLE IF NOT EXISTS shipping (
    shipping_id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id          BIGINT NOT NULL,
    buyer_id          BIGINT NOT NULL,
    shipping_status   VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    courier_name      VARCHAR(255),
    tracking_number   VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS reviews (
    review_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id   BIGINT NOT NULL,
    buyer_id     BIGINT NOT NULL,
    rating       INT NOT NULL,
    review       VARCHAR(2000),
    created_at   DATETIME
);

CREATE TABLE IF NOT EXISTS wishlist (
    wishlist_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    buyer_id     BIGINT NOT NULL,
    product_id   BIGINT NOT NULL,
    UNIQUE KEY uq_wishlist_buyer_product (buyer_id, product_id)
);

CREATE TABLE IF NOT EXISTS notifications (
    notification_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id              BIGINT NOT NULL,
    title                 VARCHAR(255) NOT NULL,
    message               VARCHAR(2000),
    notification_type    VARCHAR(50),
    is_read               BOOLEAN NOT NULL DEFAULT FALSE,
    created_at            DATETIME
);

-- ---------- Seed data ----------
-- Matches what DataSeeder.java inserts automatically on first app run.

-- Admin login: admin@globalagriculture.com / admin123
-- (password below is a BCrypt hash of "admin123")
INSERT INTO users (full_name, email, password, phone, address, city, state, country, pincode, role, created_at)
SELECT 'Global Agriculture Admin', 'admin@globalagriculture.com',
       '$2b$10$SMSBFKpjDFJLnOeYdZAfvePcTmErDwngZuvS4yE.dag273.qYngGK',
       '9999999999', 'Head Office', 'Bengaluru', 'Karnataka', 'India', '560001', 'ADMIN', NOW()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@globalagriculture.com');

INSERT INTO categories (category_name, description)
SELECT * FROM (SELECT 'Vegetables' AS n, 'Fresh farm vegetables' AS d) t
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE category_name = 'Vegetables');

INSERT INTO categories (category_name, description)
SELECT * FROM (SELECT 'Fruits', 'Seasonal fruits') t
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE category_name = 'Fruits');

INSERT INTO categories (category_name, description)
SELECT * FROM (SELECT 'Grains', 'Wheat, rice, and cereals') t
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE category_name = 'Grains');

INSERT INTO categories (category_name, description)
SELECT * FROM (SELECT 'Spices', 'Herbs and spices') t
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE category_name = 'Spices');

INSERT INTO categories (category_name, description)
SELECT * FROM (SELECT 'Pulses', 'Lentils and legumes') t
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE category_name = 'Pulses');

INSERT INTO categories (category_name, description)
SELECT * FROM (SELECT 'Dairy', 'Milk and dairy products') t
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE category_name = 'Dairy');

INSERT INTO categories (category_name, description)
SELECT * FROM (SELECT 'Herbs', 'Fresh herbs') t
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE category_name = 'Herbs');

-- Sample products
INSERT INTO products (product_name, description, price, stock, unit, image_url, country_of_origin, category_id, harvest_date, expiry_date, status)
SELECT 'Zucchini', 'Fresh green zucchini, hand-picked.', 45.00, 120, 'kg',
       '/images/products/Zucchini.jpg', 'India',
       (SELECT category_id FROM categories WHERE category_name = 'Vegetables'),
       DATE_SUB(CURDATE(), INTERVAL 1 DAY), DATE_ADD(CURDATE(), INTERVAL 6 DAY), 'Available'
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_name = 'Zucchini');

INSERT INTO products (product_name, description, price, stock, unit, image_url, country_of_origin, category_id, harvest_date, expiry_date, status)
SELECT 'Whole Wheat', 'Stone-ground whole wheat, farm sourced.', 38.00, 300, 'kg',
       '/images/products/Whole Wheat.jpg', 'India',
       (SELECT category_id FROM categories WHERE category_name = 'Grains'),
       DATE_SUB(CURDATE(), INTERVAL 10 DAY), DATE_ADD(CURDATE(), INTERVAL 180 DAY), 'Available'
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_name = 'Whole Wheat');

INSERT INTO products (product_name, description, price, stock, unit, image_url, country_of_origin, category_id, harvest_date, expiry_date, status)
SELECT 'Alphonso Mango', 'Sweet, ripe Alphonso mangoes.', 120.00, 0, 'dozen',
       '', 'India',
       (SELECT category_id FROM categories WHERE category_name = 'Fruits'),
       DATE_SUB(CURDATE(), INTERVAL 3 DAY), DATE_ADD(CURDATE(), INTERVAL 4 DAY), 'Out_of_Stock'
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_name = 'Alphonso Mango');

-- ---------- Verify ----------
SELECT 'users' AS table_name, COUNT(*) AS rows FROM users
UNION ALL SELECT 'categories', COUNT(*) FROM categories
UNION ALL SELECT 'products', COUNT(*) FROM products;
