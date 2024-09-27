-- 4. Схема таблицы Post [#1731]
create TABLE post (
    id SERIAL PRIMARY KEY ,
    "name" VARCHAR(255) ,
    "text" TEXT ,
    link VARCHAR(255) unique,
    created TIMESTAMP
);
select * from post;
