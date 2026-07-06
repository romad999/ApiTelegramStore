--liquibase formatted sql

--changeset roma:4
-- 1. Удаляем старую таблицу заказов, так как её структура меняется
DROP TABLE IF EXISTS orders CASCADE;

-- 2. Создаем новую чистую таблицу заказов (без product_id и без quantity!)
CREATE TABLE orders (
                        id BIGSERIAL PRIMARY KEY,
                        customer_name VARCHAR(255),
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Создаем промежуточную таблицу-корзину для связи Many-to-Many
CREATE TABLE order_items (
                             id BIGSERIAL PRIMARY KEY,
                             order_id BIGINT NOT NULL,
                             product_id BIGINT NOT NULL,
                             quantity INT NOT NULL,

    -- Связываем с таблицей заказов
                             CONSTRAINT fk_items_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    -- Связываем с таблицей товаров
                             CONSTRAINT fk_items_product FOREIGN KEY (product_id) REFERENCES products(id)
);
