package ru.job4j.utils;

import java.io.IOException;
import java.util.List;

/**
 * 3. Архитектура проекта - Аргегатор Java Вакансий [#260359].
 */
public interface Parse {
    List<Post> list(String link) throws IOException;

}
