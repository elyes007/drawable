package tn.disguisedtoast.drawable.homeModule.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import org.apache.commons.io.FileUtils;
import tn.disguisedtoast.drawable.ProjectMain.Drawable;
import tn.disguisedtoast.drawable.projectGenerationModule.ionic.ProjectGeneration;
import tn.disguisedtoast.drawable.storyboardModule.controllers.StoryboardViewController;
import tn.disguisedtoast.drawable.utils.EveryWhereLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class HomeLayoutController implements Initializable {

    @FXML
    BorderPane root;
    @FXML
    public Button scrollBtn;
    @FXML
    public Button storyboardBtn;
    @FXML
    public TextField search;
    @FXML
    public Button export;
    private static HomeLayoutController instance;
    @FXML
    public Button playButton;
    @FXML
    public HBox backgroundProcessIndicator;
    @FXML
    public Label processName;
    @FXML
    public SplitMenuButton projectNameMenu;
    @FXML
    public HBox doneIndicator;

    public static HomeLayoutController getInstance() {
        return instance;
    }

    private boolean getIonicState() {
        FileReader fileReader;
        String filePath = Drawable.projectPath + File.separator + "state.json";
        try {
            fileReader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            String fileBody = "{\n\t\"ionic_state\":false\n}";
            try {
                FileUtils.writeStringToFile(new File(filePath), fileBody);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return false;
        }
        JsonObject projectsJson = new JsonParser().parse(fileReader).getAsJsonObject();
        return projectsJson.get("ionic_state").getAsBoolean();
    }

    private void setIonicState(boolean state) {
        try {
            String filePath = Drawable.projectPath + File.separator + "state.json";

            JsonObject globalSettingsJson = new JsonParser().parse(filePath).getAsJsonObject();
            if (globalSettingsJson == null) {
                globalSettingsJson = new JsonObject();
            }
            globalSettingsJson.addProperty("ionic_state", state);
            Files.write(Paths.get(filePath), new Gson().toJson(globalSettingsJson).getBytes());
            System.out.println("Changed state to " + state);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportProject(ActionEvent actionEvent) {
        EveryWhereLoader.getInstance().showLoader(Drawable.globalStage);
        try {
            ProjectGeneration.generatePages();
        } catch (IOException e) {
            e.printStackTrace();
        }
        EveryWhereLoader.getInstance().stopLoader(null);
    }

    public void showStoryboard() {
        storyboardBtn.setVisible(false);
        scrollBtn.setVisible(true);
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/layouts/storyboard/storyboardView.fxml"));
            Parent parent = loader.load();
            root.setCenter(parent);
            loader.getLocation().openStream();
            StoryboardViewController storyboardController = loader.getController();
            this.search.setOnKeyReleased(event -> storyboardController.search(this.search.getText()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showScrollView() {
        storyboardBtn.setVisible(true);
        scrollBtn.setVisible(false);
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/layouts/homeLayouts/ScrollHomeLayout.fxml"));
            Parent parent = loader.load();
            root.setCenter(parent);
            loader.getLocation().openStream();
            ScrollHomeLayoutController scrollController = loader.getController();
            this.search.setOnKeyReleased(event -> scrollController.search(this.search.getText()));
        } catch (IOException e) {
            e.printStackTrace();
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
        storyboardBtn.managedProperty().bind(storyboardBtn.visibleProperty());
        scrollBtn.managedProperty().bind(scrollBtn.visibleProperty());
        showStoryboard();

        doneIndicator.managedProperty().bind(doneIndicator.visibleProperty());
        backgroundProcessIndicator.managedProperty().bind(backgroundProcessIndicator.visibleProperty());
        doneIndicator.setVisible(false);
        backgroundProcessIndicator.setVisible(false);

        //check and generate ionic project in background
        if (!getIonicState()) {
            CompletableFuture.supplyAsync(ProjectGeneration::generateBlankProject)
                    .thenAccept(this::setIonicState);
        }
    }
}
