package ru.job4j.utils;

import java.time.LocalDateTime;

/**
 * 2.1. Преобразование даты [#289476].
 */
public interface DateTimeParser {
    LocalDateTime parse(String parse);
}
