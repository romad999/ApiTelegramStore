--liquibase formatted sql

--changeset roma:1
CREATE TABLE products (
                          id BIGSERIAL PRIMARY KEY,
                          title VARCHAR(255) NOT NULL,
                          price NUMERIC(10,2),
                          quantity INT
);
