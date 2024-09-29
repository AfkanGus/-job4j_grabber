package ru.job4j.quartz;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.utils.DateTimeParser;
import ru.job4j.utils.HabrCareerDateTimeParser;
import ru.job4j.utils.Post;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 2.4. HabrCareerParse [#285213]
 */
public class HabrCareerParse {
    private static final String SOURCE_LINK = "https://career.habr.com";
    public static final String PREFIX = "/vacancies?page=";
    public static final String SUFFIX = "&q=Java%20developer&type=all";
    public static final int COUNT_PAGES = 1;
    private final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

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

    public List<Post> list(String link) throws IOException {
        List<Post> posts = new ArrayList<>();
        try {
            for (int pageNumber = 1; pageNumber <= COUNT_PAGES; pageNumber++) {
                String fullLink = "%s%s%d%s".formatted(SOURCE_LINK, PREFIX, pageNumber, SUFFIX);
                Connection connection = Jsoup.connect(fullLink);
                Document document = connection.get();
                Elements rows = document.select(".vacancy-card__inner");
                rows.forEach(row -> {
                    Element titleElement = row.select(".vacancy-card__title").first();
                    Element linkElement = titleElement.child(0);
                    String vacancyName = titleElement.text();
                    String vacancyLink = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                    Element dateElement = row.select(".vacancy-card__date time").first();
                    String date = dateElement.attr("datetime");
                    LocalDateTime dateTime = dateTimeParser.parse(date);
                    String description = retrieveDescription(vacancyLink);
                    posts.add(new Post(0, vacancyName, vacancyLink, description, dateTime));
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return posts;
    }

    public List<Post> parse() throws IOException {
        List<Post> posts = new ArrayList<>();
        for (int pageNumber = 1; pageNumber <= COUNT_PAGES; pageNumber++) {
            String fullLink = "%s%s%d%s".formatted(SOURCE_LINK, PREFIX, pageNumber, SUFFIX);
            posts.addAll(list(fullLink));
        }
        return posts;
    }

    public static void main(String[] args) throws IOException {
        DateTimeParser dateTimeParser = new HabrCareerDateTimeParser();
        HabrCareerParse habrCareerParse = new HabrCareerParse(dateTimeParser);
        List<Post> postList = habrCareerParse.list(SOURCE_LINK);
        postList.forEach(post -> System.out.printf(
                "Vacancy: %s%nLink: %s%nDate: %s%nDescription: %s%n%n",
                post.getTitle(), post.getLink(), post.getCreated(), post.getDescription()
        ));
    }
}
