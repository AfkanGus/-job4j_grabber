package ru.job4j.quartz;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * 2.1.1. Парсинг https:/career.habr.com/vacancies/java_developer?page=1.
 * По техническому заданию мы должны получить данные с сайта -
 * https:/career.habr.com/vacancies?q=Java%20developer&type=all
 * Нужно парсить первые 5 страниц.
 */
public class HabrCareerParse {
    private static final String SOURCE_LINK = "https://career.habr.com";
    public static final String PREFIX = "/vacancies?page=";
    public static final String SUFFIX = "&q=Java%20developer&type=all";
    public static final int COUNT_PAGES = 5;

    public static void main(String[] args) throws IOException {
        for (int pageNumber = 1; pageNumber <= COUNT_PAGES; pageNumber++) {
            String fullLink = "%s%s%d%s".formatted(SOURCE_LINK, PREFIX, pageNumber, SUFFIX);
            Connection connection = Jsoup.connect(fullLink);
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                String vacancyName = titleElement.text();
                String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                Element dateElement = row.select(".vacancy-card__date time").first();
                String date = dateElement.attr("datetime");
                System.out.printf("Vacancy: %s%nLink: %s%nDate: %s%n%n", vacancyName, link, date);
            });
        }
    }
}
