package tn.disguisedtoast.drawable.homeModule.controllers;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import tn.disguisedtoast.drawable.homeModule.models.Page;

import java.net.URL;
import java.util.ResourceBundle;

public class PageCellViewController implements Initializable {
    @FXML
    private ImageView pageImage;
    @FXML
    private Label pageName;
    @FXML
    private VBox pagePane;

    private Page page;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setPage(Page page, PageClickCallback callback) {
        this.page = page;
        System.out.println(page.getImage());
        //this.pageImage.setBackground(new Background(new BackgroundImage(page.getImage(), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        this.pageImage.setImage(page.getImage());
        this.pageName.setText(page.getName());

        this.pagePane.setOnMouseClicked(event -> {
            callback.clicked(page);
        });
    }

    public interface PageClickCallback {
        void clicked(Page page);
    }

    public VBox getPagePane() {
        return pagePane;
    }

    public Page getPage() {
        return page;
    }
}
