-- ============================================================
-- Reference schema for global_agriculture
-- ============================================================
-- You do NOT need to run this by hand: with
--   spring.jpa.hibernate.ddl-auto=update
-- Hibernate creates/updates every one of these tables automatically
-- the first time you run the app (and DataSeeder.java seeds an admin
-- account + categories + a few sample products).
--
-- This file is just here so you can see the shape of the schema, or
-- run it yourself if you'd rather manage the DB by hand
-- (set ddl-auto=validate or none in that case).
-- ============================================================

CREATE DATABASE IF NOT EXISTS global_agriculture;
USE global_agriculture;

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
