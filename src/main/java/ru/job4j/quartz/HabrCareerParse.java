package ru.job4j.quartz;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.Doc;
import java.io.IOException;

/**
 * 2.3. Загрузка деталей поста. [#285212]
 */
public class HabrCareerParse {
    private static final String SOURCE_LINK = "https://career.habr.com";
    public static final String PREFIX = "/vacancies?page=";
    public static final String SUFFIX = "&q=Java%20developer&type=all";
    public static final int COUNT_PAGES = 1;

    private static String retrieveDescription(String link) {
        try {
            Document vacancyPage = Jsoup.connect(link).get();
            Element descriptionElement = vacancyPage.select(".style-ugc").first();
            if (descriptionElement != null) {
                return descriptionElement.text();
            } else {
                return "Description not found";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Failed to retrieve description";
    }

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
                String description = retrieveDescription(link);
                System.out.printf("Vacancy: %s%nLink: %s%nDate: %s%nDescription: %s%n%n",
                        vacancyName, link, date, description);
            });
        }
    }
}
