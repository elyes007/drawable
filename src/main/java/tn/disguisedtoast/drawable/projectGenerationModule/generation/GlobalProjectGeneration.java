package tn.disguisedtoast.drawable.projectGenerationModule.generation;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import org.apache.commons.io.FileUtils;
import tn.disguisedtoast.drawable.ProjectMain.Drawable;
import tn.disguisedtoast.drawable.utils.EveryWhereLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class GlobalProjectGeneration implements Initializable {
    @FXML
    public Button newProject;
    @FXML
    public Button openProject;
    private static String splitn;
    private static String projectPath;
    @FXML
    public BorderPane startPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (checkCurrentProject()) return;
        this.newProject.setOnMouseClicked(event -> createNewProject());
        this.openProject.setOnMouseClicked(event -> openProject());
    }

    private boolean checkCurrentProject() {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader("./src/main/projects.json");
        } catch (FileNotFoundException e) {
            String fileBody = "{\n\t\"current\":null,\n\t\"recent\":[]\n}";
            try {
                FileUtils.writeStringToFile(new File("./src/main/projects.json"), fileBody);
                fileReader = new FileReader("./src/main/projects.json");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        JsonObject projectsJson = new JsonParser().parse(fileReader).getAsJsonObject();
        if (!projectsJson.get("current").isJsonNull()) {
            Drawable.projectPath = projectsJson.get("current").getAsString();
            EveryWhereLoader.getInstance().showLoader(Drawable.globalStage);
            showHome();
            return true;
        }
        return false;
    }

    //TODO: check if project structure is respected before opening
    private void openProject() {
        DirectoryChooser dc = new DirectoryChooser();
        File f = dc.showDialog(Drawable.globalStage);
        String path = f.getAbsolutePath();
        Drawable.projectPath = projectPath = path;
        updateCurrentProject();
        EveryWhereLoader.getInstance().showLoader(Drawable.globalStage);
        showHome();
    }

    private void createNewProject() {
        DirectoryChooser dc = new DirectoryChooser();
        File f = dc.showDialog(Drawable.globalStage);
        String s = f.getAbsolutePath();
        System.out.println(s);
        try {
            splitn = dialogSplit();
            System.out.println(splitn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        projectPath = s + File.separator + splitn;
        Drawable.projectPath = projectPath;

        EveryWhereLoader.getInstance().showLoader(Drawable.globalStage);
        CompletableFuture
                .runAsync(GlobalProjectGeneration::createprojectHierarchy)
                .thenAccept(aVoid -> {
                    updateCurrentProject();
                    showHome();
                });
    }

    private void updateCurrentProject() {
        try {
            JsonObject projectsJson = new JsonParser().parse(new FileReader("./src/main/projects.json"))
                    .getAsJsonObject();
            projectsJson.addProperty("current", projectPath);
            FileUtils.writeStringToFile(new File("./src/main/projects.json"), projectsJson.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showHome() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/homeLayouts/HomeLayout.fxml"));
        try {
            EveryWhereLoader.getInstance().stopLoader(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createprojectHierarchy() {
        new File(Drawable.projectPath).mkdir();
        new File(projectPath + "\\RelatedFiles").mkdir();
        new File(projectPath + "\\RelatedFiles\\previewModule").mkdir();
        new File(projectPath + "\\RelatedFiles\\assets").mkdir();
        new File(projectPath + "\\RelatedFiles\\pages").mkdir();
        try {
            FileUtils.copyDirectory(new File(System.getProperty("user.dir") + "\\src\\main\\RelatedFiles\\previewModule"),
                    new File(projectPath + "\\RelatedFiles\\previewModule"));
            FileUtils.copyToDirectory(new File(System.getProperty("user.dir") + "\\src\\main\\RelatedFiles\\generated_views\\assets\\drawable\\placeholder.png"),new File(projectPath + "\\RelatedFiles\\assets"));
            FileUtils.writeStringToFile(new File(Drawable.projectPath + File.separator + "state.json"),
                    "{\n\t\"ionic_state\":false\n}");
            System.out.println("Done!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String dialogSplit() throws IOException {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Drawable Project Name");
        dialog.setHeaderText("Enter your project title");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(s -> splitn = s);
        return splitn;
    }


}
