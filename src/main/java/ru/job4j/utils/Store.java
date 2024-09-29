package ru.job4j.utils;

import java.util.List;

/**
 * 3. Архитектура проекта - Аргегатор Java Вакансий [#260359].
 */
public interface Store extends AutoCloseable {
    void save(Post post);

    List<Post> getAll();

    Post findById(int id);

    void close() throws Exception;
}
