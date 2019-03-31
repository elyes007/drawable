package tn.disguisedtoast.drawable.homeModule.controllers;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import tn.disguisedtoast.drawable.homeModule.models.Page;
import tn.disguisedtoast.drawable.utils.ImageViewPane;

import java.net.URL;
import java.util.ResourceBundle;

public class PageCellViewController implements Initializable {
    @FXML
    private AnchorPane pageImagePane;
    @FXML
    private ImageView pageImage;
    @FXML
    private Label pageName;
    @FXML
    private BorderPane pagePane;

    private Page page;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setPage(Page page, PageClickCallback callback) {
        this.page = page;

        ImageView imageView = new ImageView(page.getImage());
        imageView.setPreserveRatio(true);
        ImageViewPane imageViewPane = new ImageViewPane(imageView);

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
