package tn.disguisedtoast.drawable.homeModule.controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import org.apache.commons.io.FileUtils;
import tn.disguisedtoast.drawable.ProjectMain.Drawable;
import tn.disguisedtoast.drawable.storyboardModule.controllers.StoryboardViewController;

import java.io.*;
import java.net.URL;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        storyboardBtn.managedProperty().bind(storyboardBtn.visibleProperty());
        scrollBtn.managedProperty().bind(scrollBtn.visibleProperty());
        showStoryboard();

        //check and generate ionic project in background
        /*if(!getIonicState()){
            CompletableFuture.supplyAsync(ProjectGeneration::generateBlankProject)
                    .thenAccept(this::setIonicState);
        }*/
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
        String filePath = Drawable.projectPath + File.separator + "state.json";
        String fileBody = "{\n\t\"ionic_state\":" + state + "\n}";
        try {
            FileUtils.writeStringToFile(new File(filePath), fileBody);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void exportProject(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            DirectoryChooser dc = new DirectoryChooser();
            //dc.showDialog(primaryStage);
            File f = dc.showDialog(Drawable.globalStage);
            String s = f.getAbsolutePath();
            System.out.println(s);

            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.directory(new File(s));
            // TextField projectName = new TextField();


            processBuilder.command("cmd.exe", "/c", "ionic start testProject blank");
            try {

                Process process = processBuilder.start();

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("wait");
                    System.out.println(line);
                }

                int exitCode = process.waitFor();
                System.out.println("\nExited with error code : " + exitCode);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
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
}
