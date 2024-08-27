package ru.job4j.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 2.1. Преобразование даты [#289476].
 */
class HabrCareerDateTimeParserTest {
    @Test
    void whenParseDateTimeThenCorrectLocalDateTime() {
        DateTimeParser parser = new HabrCareerDateTimeParser();
        String dateStr = "2024-08-24T15:15:22+03:00";
        LocalDateTime expected = LocalDateTime.of(2024, 8, 24, 15, 15, 22);
        LocalDateTime result = parser.parse(dateStr);
        assertEquals(expected, result);
    }

    @Test
    void whenParseDateWithoutTimeZoneThenCorrectLocalDateTime() {
        DateTimeParser parser = new HabrCareerDateTimeParser();
        String dateStr = "2023-08-24T15:34:22";
        LocalDateTime expected = LocalDateTime.of(2023, 8, 24, 15, 34, 22);
        LocalDateTime result = parser.parse(dateStr);
        assertEquals(expected, result);
    }

    @Test
    void whenParseInvalidDateThenThrowsException() {
        DateTimeParser parser = new HabrCareerDateTimeParser();
        String invalidDateStr = "invalid-date-format";
        assertThrows(Exception.class, () -> parser.parse(invalidDateStr));
    }
}