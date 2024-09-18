package ru.job4j.utils;

import org.quartz.SchedulerException;

/**
 * 3. Архитектура проекта - Аргегатор Java Вакансий [#260359].
 */
public interface Grab {
    void init() throws SchedulerException;
}
