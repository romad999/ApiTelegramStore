--liquibase formatted sql

--changeset roma:3
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL
);

-- Сразу добавим одного дефолтного админа, чтобы не остаться без доступа.
-- Пароль ниже — это захешированное слово "admin123" через алгоритм BCrypt!
INSERT INTO users (username, password, role)
VALUES ('roma_admin', '$2a$10$dXJ3Zw6Z99N99Z9999999O9999999999999999999999999999999', 'ROLE_ADMIN');
