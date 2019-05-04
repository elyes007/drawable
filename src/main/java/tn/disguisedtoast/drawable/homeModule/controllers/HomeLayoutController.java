package tn.disguisedtoast.drawable.homeModule.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import tn.disguisedtoast.drawable.ProjectMain.Drawable;
import tn.disguisedtoast.drawable.ProjectMain.GlobalViewController;
import tn.disguisedtoast.drawable.projectGenerationModule.ionic.ProjectGeneration;
import tn.disguisedtoast.drawable.storyboardModule.controllers.StoryboardViewController;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
    @FXML
    public Button playButton;
    public static boolean generationInProcess;


    private StoryboardViewController storyboardController;
    private ScrollHomeLayoutController scrollHomeController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        storyboardBtn.managedProperty().bind(storyboardBtn.visibleProperty());
        scrollBtn.managedProperty().bind(scrollBtn.visibleProperty());
        showStoryboard();
        playButton.setOnMouseClicked(event -> {

            new Thread(() -> {
                try {
                    playApp();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            // playApp();

        });
        System.out.println(Drawable.globalStage);
        /*Drawable.height = Drawable.globalStage.getScene().getHeight();
        Drawable.width = Drawable.globalStage.getScene().getWidth();*/
    }

    public void exportProject(ActionEvent actionEvent) {
        ProjectGeneration.generatePages();
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

    public static boolean playApp() throws IOException {
        generationInProcess = true;
        GlobalViewController.BackgroundProcess backgroundProcess = GlobalViewController.startBackgroundProcess(new GlobalViewController.BackgroundProcess("Serving IONIC project.", null));
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(Drawable.projectPath + "\\ionic_project"));
        processBuilder.command("cmd.exe", "/c", "ionic serve --lab");
        backgroundProcess.setProcess(processBuilder.start());
        BufferedReader reader = new BufferedReader(new InputStreamReader(backgroundProcess.getProcess().getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        int exitCode = backgroundProcess.getProcess().exitValue();
        System.out.println("\nExited with exit code : " + exitCode);
        generationInProcess = false;

        GlobalViewController.stopBackgroundProcess(backgroundProcess);
        return exitCode == 0;


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
