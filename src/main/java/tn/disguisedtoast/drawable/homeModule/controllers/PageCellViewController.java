package tn.disguisedtoast.drawable.homeModule.controllers;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import tn.disguisedtoast.drawable.homeModule.models.Page;
import tn.disguisedtoast.drawable.storyboardModule.controllers.StoryboardViewController;
import tn.disguisedtoast.drawable.utils.ImageViewPane;

import java.net.URL;
import java.util.ResourceBundle;

public class PageCellViewController implements Initializable {
    @FXML
    private Label pageName;
    @FXML
    private BorderPane pagePane;
    @FXML
    private Button deleteButton;

    private ImageViewPane imageViewPane;
    private ImageView imageView;
    private ScrollHomeLayoutController scrollHomeLayoutController;
    private Page page;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.pagePane.setOnMouseEntered(event -> {
            this.deleteButton.setVisible(true);
        });
        this.pagePane.setOnMouseExited(event -> {
            this.deleteButton.setVisible(false);
        });
        this.deleteButton.setOnMouseClicked(event -> {
            Pane parent = (Pane) this.pagePane.getParent();
            parent.getChildren().clear();

            //Detaching image
            releaseImage();
            System.gc();

            new StoryboardViewController.JSCallback().deletePage(page.getFolderName());

            scrollHomeLayoutController.refresh();
        });

    }

    public void releaseImage() {
        this.imageViewPane.setImageView(null);
        this.imageViewPane = null;
        this.imageView.setImage(null);
        this.imageView = null;
        this.page.setImage(null);
    }

    public void setPage(Page page, PageClickCallback callback, ScrollHomeLayoutController scrollHomeLayoutController) {
        this.page = page;
        this.scrollHomeLayoutController = scrollHomeLayoutController;

        imageView = new ImageView(page.getImage());
        imageView.setPreserveRatio(true);
        imageViewPane = new ImageViewPane(imageView);

        pagePane.setCenter(imageViewPane);

        this.pageName.setText(page.getName());

        this.pagePane.setOnMouseClicked(event -> {
            callback.clicked(page);
        });
    }

    public interface PageClickCallback {
        void clicked(Page page);
    }

    public Pane getPagePane() {
        return pagePane;
    }

    public Page getPage() {
        return page;
    }
}
