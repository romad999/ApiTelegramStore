-- Liquibase formatted sql
-- changeset roma:5

ALTER TABLE orders ADD COLUMN user_id BIGINT;

-- Создаем связь (Foreign Key), чтобы нельзя было создать заказ на несуществующего юзера
ALTER TABLE orders
    ADD CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

-- Старую колонку customer_name пока можно не удалять, либо удалить позже, когда все перепишем