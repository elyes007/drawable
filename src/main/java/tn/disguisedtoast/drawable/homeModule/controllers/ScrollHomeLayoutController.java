package tn.disguisedtoast.drawable.homeModule.controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import tn.disguisedtoast.drawable.ProjectMain.Drawable;
import tn.disguisedtoast.drawable.detectionModule.controllers.CamChooserController;
import tn.disguisedtoast.drawable.detectionModule.controllers.CamStreamViewController;
import tn.disguisedtoast.drawable.homeModule.models.Page;
import tn.disguisedtoast.drawable.settingsModule.controllers.SettingsViewController;
import tn.disguisedtoast.drawable.utils.EveryWhereLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ScrollHomeLayoutController implements CamChooserController.CameraButtonCallback, Initializable {
    @FXML
    public AnchorPane addButtonPane;
    @FXML
    private HBox pagesPreviewHBox;

    public static Stage primaryStage;
    private List<PageCellViewController> pageCellViewControllers;
    private static String pagesPath = (Drawable.projectPath + "&RelatedFiles&pages").replace("&", File.separator);

    private PageCellViewController.PageClickCallback pageClickCallback = page -> {
        try {
            EveryWhereLoader.getInstance().showLoader(Drawable.globalStage);
            FXMLLoader loader = new FXMLLoader(SettingsViewController.class.getResource("/layouts/settingsViews/SettingsView.fxml"));
            EveryWhereLoader.getInstance().stopLoader(loader.load());
            SettingsViewController controller = loader.getController();
            controller.init(page.getFolderName());
        } catch (IOException e) {
            e.printStackTrace();
            EveryWhereLoader.getInstance().stopLoader(null);
        }
    };
    private Stage chooserStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ((Button) this.addButtonPane.getChildren().get(0)).setOnAction(event -> {
            EveryWhereLoader.getInstance().showLoader(Drawable.globalStage);
            chooserStage = new Stage();
            chooserStage.setTitle("Camera Chooser");
            chooserStage.setScene(new Scene(new CamChooserController(this).getRoot()));
            chooserStage.setHeight(200);
            chooserStage.setWidth(500);
            chooserStage.setResizable(false);
            chooserStage.centerOnScreen();
            chooserStage.show();
            EveryWhereLoader.getInstance().stopLoader(null);
        });

        refresh();
    }

    @Override
    public void onButtonClicked(int webcamIndex) {
        chooserStage.close();
        if (webcamIndex != -1) {
            try {
                EveryWhereLoader.getInstance().showLoader(Drawable.globalStage);
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/layouts/detectionViews/CamStreamView.fxml"));
                loader.getLocation().openStream();
                EveryWhereLoader.getInstance().stopLoader(loader.load());
                CamStreamViewController controller = loader.getController();
                controller.init(webcamIndex);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<Page> loadPages() {
        File root = new File(pagesPath);
        String[] directories = root.list((dir, name) -> (new File(dir, name).isDirectory()));
        List<Page> pages = new ArrayList<>();
        for (String dir : directories) {
            if (dir.equals("temp")) continue;
            try {
                JsonObject jsonObject = new JsonParser().parse(new FileReader(pagesPath + "/" + dir + "/conf.json")).getAsJsonObject();
                String pageName = jsonObject.get("page").getAsString();
                Page pg = new Page(pageName, pagesPath + File.separator + dir);
                pages.add(pg);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        pages.sort((p1, p2) -> Integer.parseInt(p1.getFolderName().substring(pagesPath.length() + 5)) > Integer.parseInt(p2.getFolderName().substring(pagesPath.length() + 5)) ? 1 : -1);
        return pages;
    }

    public void refresh() {
        pagesPreviewHBox.getChildren().clear();
        pagesPreviewHBox.getChildren().add(this.addButtonPane);
        pageCellViewControllers = new ArrayList<>();
        List<Page> pages = loadPages();
        for (Page page : pages) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/homeLayouts/PageCellView.fxml"));
                Pane pagePane = loader.load();
                PageCellViewController pageCellViewController = loader.getController();
                pageCellViewController.setPage(page, pageClickCallback, this);
                pageCellViewControllers.add(pageCellViewController);
                pagesPreviewHBox.getChildren().addAll(pagePane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void search(String text) {
        if (text.isEmpty()) {
            Platform.runLater(() -> {
                this.pagesPreviewHBox.getChildren().clear();
                this.pagesPreviewHBox.getChildren().add(addButtonPane);
                this.pagesPreviewHBox.getChildren().addAll(
                        this.pageCellViewControllers.stream().map(
                                pageCellViewController -> pageCellViewController.getPagePane()
                        ).collect(Collectors.toList())
                );
            });
        } else {
            Platform.runLater(() -> {
                this.pagesPreviewHBox.getChildren().clear();
                this.pagesPreviewHBox.getChildren().addAll(
                        this.pageCellViewControllers.stream().filter(
                                pageCellViewController -> pageCellViewController.getPage().getName().contains(text)
                        ).map(
                                pageCellViewController -> pageCellViewController.getPagePane()
                        ).collect(Collectors.toList())
                );
            });
        }
    }

    public void releaseImages() {
        for (PageCellViewController pageCell : pageCellViewControllers) {
            pageCell.releaseImage();
            System.gc();
        }
    }
}
