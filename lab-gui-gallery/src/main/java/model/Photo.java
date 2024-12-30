package model;

import javafx.beans.property.*;
import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.util.UUID;


public class Photo {

    private final ObjectProperty<Image> photoData;
    private StringProperty name;

    public Photo(String extension, byte[] photoData) {
        this.photoData = new SimpleObjectProperty<>(new Image(new ByteArrayInputStream(photoData)));
        this.name = new SimpleStringProperty(UUID.randomUUID().toString() + "." + extension);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public Image getPhotoData() {
        return photoData.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public ReadOnlyObjectProperty<Image> photoDataProperty() {
        return photoData;
    }
}
