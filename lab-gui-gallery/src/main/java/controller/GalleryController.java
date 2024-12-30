package controller;


import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import model.Gallery;
import model.Photo;
import org.pdfsam.rxjavafx.schedulers.JavaFxScheduler;
import util.PhotoDownloader;

public class GalleryController {
    @FXML
    private TextField searchTextField;
    @FXML
    private ListView<Photo> imagesListView;
    @FXML
    private ImageView imageView;
    @FXML
    private TextField imageNameField;
    private Gallery galleryModel;

    @FXML
    public void initialize() {
        // TODO additional FX controls initialization
        imagesListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Photo item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    ImageView photoIcon = new ImageView(item.getPhotoData());
                    photoIcon.setPreserveRatio(true);
                    photoIcon.setFitHeight(50);
                    setGraphic(photoIcon);
                }
            }
        });

        imagesListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    bindSelectedPhoto(newValue);
                    if (oldValue != null)
                        imageNameField.textProperty().unbindBidirectional(oldValue.nameProperty());
                });
    }

    public void setModel(Gallery gallery) {
        this.galleryModel = gallery;
//        bindSelectedPhoto(imagesListView.getSelectionModel().select);
        imagesListView.getSelectionModel().select(0);
        imagesListView.setItems(gallery.photosProperty());
    }

    private void bindSelectedPhoto(Photo selectedPhoto) {
        // TODO view <-> model bindings configuration
        imageView.imageProperty().bind(selectedPhoto.photoDataProperty());
        imageNameField.textProperty().bindBidirectional(selectedPhoto.nameProperty());
    }

    public void searchButtonClicked(ActionEvent event) {
        PhotoDownloader photoDownloader = new PhotoDownloader();
        galleryModel.clear();
        photoDownloader.searchForPhotos(searchTextField.getText())
                .subscribeOn(Schedulers.io())
                .take(5)
                .observeOn(JavaFxScheduler.platform())
                .subscribe(photo -> galleryModel.addPhoto(photo));
    }
}

