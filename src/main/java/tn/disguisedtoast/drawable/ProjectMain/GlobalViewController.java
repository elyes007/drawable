package tn.disguisedtoast.drawable.ProjectMain;

import com.google.gson.*;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import tn.disguisedtoast.drawable.utils.EveryWhereLoader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class GlobalViewController implements Initializable {

    private static GlobalViewController instance;
    @FXML
    public HBox backgroundProcessIndicator;
    @FXML
    public Label processName;
    @FXML
    public SplitMenuButton projectNameMenu;
    @FXML
    public HBox doneIndicator;
    @FXML
    public BorderPane globalViewPane;
    @FXML
    public MenuItem closeProjectuButton;

    public static void show(Parent parent) {
        if (instance != null) {
            instance.globalViewPane.setCenter(parent);
        }
    }

    public static void startBackgroundProcess(String name) {
        if (instance != null && instance.projectNameMenu.isVisible()) {
            Platform.runLater(() -> {
                instance.processName.setText(name);
                instance.backgroundProcessIndicator.setVisible(true);
            });
        }
    }

    public static void stopBackgroundProcess() {
        if (instance != null && instance.projectNameMenu.isVisible()) {
            new Thread(() -> {
                Platform.runLater(() -> {
                    instance.backgroundProcessIndicator.setVisible(false);
                    instance.doneIndicator.setVisible(true);
                });

                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Platform.runLater(() -> {
                    FadeTransition ft = new FadeTransition(Duration.millis(3000), instance.doneIndicator);
                    ft.setFromValue(1.0);
                    ft.setToValue(0.0);

                    ft.setOnFinished(event -> {
                        instance.doneIndicator.setVisible(false);
                    });
                    ft.play();
                });
            }).start();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;

        doneIndicator.managedProperty().bind(doneIndicator.visibleProperty());
        backgroundProcessIndicator.managedProperty().bind(backgroundProcessIndicator.visibleProperty());
        doneIndicator.setVisible(false);
        backgroundProcessIndicator.setVisible(false);

        projectNameMenu.setText(Paths.get(Drawable.projectPath).getFileName().toString());

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/homeLayouts/HomeLayout.fxml"));
            globalViewPane.setCenter(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        closeProjectuButton.setOnAction(event -> {
            EveryWhereLoader.getInstance().showLoader(Drawable.globalStage);
            try {
                String filePath = System.getProperty("user.dir") + "&src&main&projects.json".replaceAll("&", "\\" + File.separator);
                JsonObject jsonObject = new JsonParser().parse(new FileReader(filePath)).getAsJsonObject();
                jsonObject.add("current", JsonNull.INSTANCE);
                Gson gson = new GsonBuilder().serializeNulls().create();
                Files.write(Paths.get(filePath), gson.toJson(jsonObject).getBytes());

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/projectGenerationViews/startView.fxml"));
                EveryWhereLoader.getInstance().stopLoaderAndRefresh(loader.load(), () -> {
                    Drawable.globalStage.setMaximized(false);
                });
            } catch (IOException e) {
                e.printStackTrace();
                EveryWhereLoader.getInstance().stopLoader(null);
            }
        });
    }
}
