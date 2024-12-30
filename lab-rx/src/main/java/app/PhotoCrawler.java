package app;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import model.Photo;
import model.PhotoSize;
import util.PhotoDownloader;
import util.PhotoProcessor;
import util.PhotoSerializer;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PhotoCrawler {

    private static final Logger log = Logger.getLogger(PhotoCrawler.class.getName());

    private final PhotoDownloader photoDownloader;

    private final PhotoSerializer photoSerializer;

    private final PhotoProcessor photoProcessor;

    public PhotoCrawler() throws IOException {
        this.photoDownloader = new PhotoDownloader();
        this.photoSerializer = new PhotoSerializer("./photos");
        this.photoProcessor = new PhotoProcessor();
    }

    public void resetLibrary() throws IOException {
        photoSerializer.deleteLibraryContents();
    }

    public void downloadPhotoExamples() {
        try {
            photoDownloader
                    .getPhotoExamples()
                    .compose(processPhotos())
                    .subscribe(photoSerializer::savePhoto,
                            error -> log.log(Level.SEVERE, "Downloading photo examples error", error),
                            () -> log.info("Downloading photo examples completed")
                    );
        } catch (IOException e) {
            log.log(Level.SEVERE, "Downloading photo examples error", e);
        }
    }

    public void downloadPhotosForQuery(String query) throws IOException {
        photoDownloader
                .searchForPhotos(query)
                .compose(processPhotos())
                .subscribe(photoSerializer::savePhoto);
    }

    public void downloadPhotosForMultipleQueries(List<String> queries) {
        Observable
                .fromIterable(queries)
                .flatMap(photoDownloader::searchForPhotos)
                .compose(processPhotos())
                .subscribe(photoSerializer::savePhoto);
    }

    public ObservableTransformer<Photo, Photo> processPhotos() {
        return photos -> photos
                .groupBy(PhotoSize::resolve)
                .flatMap(group ->
                        switch (group.getKey()) {
                            case SMALL -> Observable.empty();
                            case PhotoSize.MEDIUM -> group.buffer(5, TimeUnit.SECONDS)
                                    .flatMap(Observable::fromIterable);
                            case PhotoSize.LARGE -> group.observeOn(Schedulers.computation())
                                    .map(photo -> {
                                        try {
                                            return photoProcessor.convertToMiniature(photo);
                                        } catch (IOException e) {
                                            return photo;
                                        }
                                    });
                            case null -> Observable.empty();
                        });
    }
}
