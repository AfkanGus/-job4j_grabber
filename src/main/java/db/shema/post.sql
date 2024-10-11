-- 4. Схема таблицы Post [#1731]
CREATE TABLE post (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    link text UNIQUE,
    created TIMESTAMP
);
select * from post;
