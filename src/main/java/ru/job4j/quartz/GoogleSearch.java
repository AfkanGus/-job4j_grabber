package ru.job4j.quartz;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GoogleSearch {

    private static final String GOOGLE_SEARCH_URL = "https://www.google.com/search";
    private static final int MAX_RESULTS = 100;

    public static void main(String[] args) {
        String query = "Vagzali";
        List<String> results = getGoogleSearchResults(query);

        String filePath = "C:/projects/job4j_grabber/google_search_results.txt";
        writeResultsToFile(results, filePath);
    }

    private static List<String> getGoogleSearchResults(String query) {
        List<String> results = new ArrayList<>();
        try {
            String searchURL = GOOGLE_SEARCH_URL + "?q=" + query + "&num=" + MAX_RESULTS + "&tbs=sbd:d";
            Document doc = Jsoup.connect(searchURL)
                    .userAgent("Edg/127.0.2651.74")
                    .get();
            Elements links = doc.select("a[href]");

            for (Element link : links) {
                String url = link.attr("href");
                if (url.startsWith("/url?q=")) {
                    url = url.substring(7, url.indexOf("&"));
                    results.add(url);
                    if (results.size() >= MAX_RESULTS) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    private static void writeResultsToFile(List<String> results, String filePath) {
         try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String result : results) {
                writer.write(result);
                writer.newLine();
            }
            System.out.println("Results saved to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
/**
 * tbs=sbd:1 используется в поисковых запросах Google для сортировки результатов.
 * Однако, для сортировки по дате, правильный параметр
 * будет tbs=qdr:d, tbs=qdr:w, tbs=qdr:m, или tbs=qdr:y, где:
 * <p>
 * tbs=qdr:d сортирует результаты за последний день.
 * tbs=qdr:w сортирует результаты за последнюю неделю.
 * tbs=qdr:m сортирует результаты за последний месяц.
 * tbs=qdr:y сортирует результаты за последний год.
 */
