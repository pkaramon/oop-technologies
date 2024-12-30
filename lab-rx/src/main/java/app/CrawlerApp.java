package app;

import java.io.IOException;
import java.util.List;

public class CrawlerApp {

    public static final String SCRAPER_API_KEY = "6aeaa49d8666469421f17bc730db8684";

    private static final List<String> TOPICS = List.of("Agent Cooper", "Sherlock", "Poirot", "Detective Monk");


    public static void main(String[] args) throws IOException {
        PhotoCrawler photoCrawler = new PhotoCrawler();
        photoCrawler.resetLibrary();
//        photoCrawler.downloadPhotoExamples();
//        photoCrawler.downloadPhotosForQuery(TOPICS.get(0));
        photoCrawler.downloadPhotosForMultipleQueries(TOPICS);

        try {
            Thread.sleep(100_000);
        } catch (InterruptedException ignored) {
        }
    }
}