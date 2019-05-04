package tn.disguisedtoast.drawable.homeModule.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
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
    @FXML
    public Button playButton;

    private StoryboardViewController storyboardController;
    private ScrollHomeLayoutController scrollHomeController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        storyboardBtn.managedProperty().bind(storyboardBtn.visibleProperty());
        scrollBtn.managedProperty().bind(scrollBtn.visibleProperty());
        showStoryboard();

        //check and generate ionic project in background
        if (!getIonicState()) {
            CompletableFuture.supplyAsync(ProjectGeneration::generateBlankProject)
                    .thenAccept(this::setIonicState);
        }
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
        if (scrollHomeController != null) {
            scrollHomeController.releaseImages();
            scrollHomeController = null;
        }
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/layouts/storyboard/storyboardView.fxml"));
            Parent parent = loader.load();
            root.setCenter(parent);
            loader.getLocation().openStream();
            storyboardController = loader.getController();
            this.search.setOnKeyReleased(event -> storyboardController.search(this.search.getText()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showScrollView() {
        storyboardBtn.setVisible(true);
        scrollBtn.setVisible(false);
        storyboardController = null;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/layouts/homeLayouts/ScrollHomeLayout.fxml"));
            Parent parent = loader.load();
            root.setCenter(parent);
            loader.getLocation().openStream();
            scrollHomeController = loader.getController();
            this.search.setOnKeyReleased(event -> scrollHomeController.search(this.search.getText()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
