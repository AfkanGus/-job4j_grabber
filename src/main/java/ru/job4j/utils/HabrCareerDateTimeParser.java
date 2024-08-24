package ru.job4j.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 2.1. Преобразование даты [#289476].
 */
public class HabrCareerDateTimeParser implements DateTimeParser {

    @Override
    public LocalDateTime parse(String parse) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        return LocalDateTime.parse(parse, formatter);
    }
}
