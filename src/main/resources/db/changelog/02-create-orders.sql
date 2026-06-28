--liquibase formatted sql

--changeset roma:2
CREATE TABLE orders (
                        id BIGSERIAL PRIMARY KEY,
                        product_id BIGINT NOT NULL,
                        quantity INT NOT NULL,
                        customer_name VARCHAR(255),
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Создаем связь: колонка product_id смотрит на id в таблице products
                        CONSTRAINT fk_order_product FOREIGN KEY (product_id) REFERENCES products(id)
);
