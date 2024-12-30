package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class Gallery {

    private final ObservableList<Photo> photos = FXCollections.observableArrayList();

    public void addPhoto(Photo photo) {
        photos.add(photo);
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public ObservableList<Photo> photosProperty() {
        return photos;
    }

    public void clear() {
        photos.clear();
    }
}
