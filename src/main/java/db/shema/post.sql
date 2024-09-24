--  4. Схема таблицы Post [#1731]
CREATE TABLE post (
    id SERIAL ,
    "name" VARCHAR(255) ,
    "text" TEXT ,
    link VARCHAR(255) ,
    created TIMESTAMP
);
SELECT * FROM post;
