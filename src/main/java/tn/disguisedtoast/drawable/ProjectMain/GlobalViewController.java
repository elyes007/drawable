package tn.disguisedtoast.drawable.ProjectMain;

import com.google.gson.*;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Menu;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GlobalViewController implements Initializable {

    private static GlobalViewController instance;
    @FXML
    public HBox backgroundProcessIndicator;
    @FXML
    public Menu processName;
    @FXML
    public SplitMenuButton projectNameMenu;
    @FXML
    public HBox doneIndicator;
    @FXML
    public BorderPane globalViewPane;
    @FXML
    public MenuItem closeProjectuButton;

    private List<BackgroundProcess> tasks = new ArrayList<>();

    public static void show(Parent parent) {
        if (instance != null) {
            instance.globalViewPane.setCenter(parent);
        }
    }

    public static BackgroundProcess startBackgroundProcess(BackgroundProcess backgroundProcess) {
        if (instance != null && instance.projectNameMenu.isVisible()) {
            instance.tasks.add(backgroundProcess);
            Platform.runLater(() -> {
                String processName = backgroundProcess.name;
                instance.processName.getItems().clear();
                System.out.println("Menu Size: " + instance.tasks.size());
                if (instance.tasks.size() > 1) {
                    processName = instance.tasks.size() + " processes running in background.";
                    for (BackgroundProcess task : instance.tasks) {
                        instance.processName.getItems().add(new MenuItem(task.name));
                    }
                }
                System.out.println(processName);
                instance.processName.setText(processName);
                instance.backgroundProcessIndicator.setVisible(true);
            });
        }
        return backgroundProcess;
    }

    public static void stopBackgroundProcess(BackgroundProcess backgroundProcess) {
        if (instance != null && instance.projectNameMenu.isVisible()) {
            Platform.runLater(() -> {
                instance.tasks.remove(backgroundProcess);
                instance.processName.getItems().clear();
                if (instance.tasks.size() == 0) {
                    instance.backgroundProcessIndicator.setVisible(false);
                    instance.doneIndicator.setVisible(true);

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
                } else if (instance.tasks.size() > 1) {
                    instance.processName.setText(instance.tasks.size() + " processes running in background.");
                    for (BackgroundProcess task : instance.tasks) {
                        instance.processName.getItems().add(new MenuItem(task.name));
                    }
                } else {
                    instance.processName.setText(instance.tasks.get(0).name);
                }
            });
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

                for (BackgroundProcess task : tasks) {
                    if (task.getProcess() != null) {
                        task.getProcess().destroy();
                    }
                }

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

    public static class BackgroundProcess {
        String name;
        Process process;

        public BackgroundProcess(String name, Process process) {
            this.name = name;
            this.process = process;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Process getProcess() {
            return process;
        }

        public void setProcess(Process process) {
            this.process = process;
        }
    }
}
