package tn.disguisedtoast.drawable.projectGenerationModule.generation;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.DirectoryChooser;
import org.apache.commons.io.FileUtils;
import tn.disguisedtoast.drawable.ProjectMain.Drawable;
import tn.disguisedtoast.drawable.utils.EveryWhereLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class GlobalProjectGeneration implements Initializable {
    @FXML
    public Button newProject;
    @FXML
    public Button openProject;
    @FXML
    public BorderPane startPane;
    @FXML
    public VBox recentVBox;
    @FXML
    public Label openProjectLink;
    @FXML
    public Label createProjectLink;
    @FXML
    public AnchorPane menuPane;
    @FXML
    public BorderPane subMenu;


    private String splitn;
    private String projectPath;
    private static List<String> recentList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (checkCurrentProject()) return;
        this.newProject.setOnMouseClicked(event -> createNewProject());
        this.openProject.setOnMouseClicked(event -> openProject());
        this.createProjectLink.setOnMouseEntered(event -> {
            createProjectLink.setTextFill(Paint.valueOf("#e28d38"));
            createProjectLink.setCursor(Cursor.HAND);

        });
        this.createProjectLink.setOnMouseExited(event -> {
            createProjectLink.setTextFill(Paint.valueOf("#3c3c3c"));
        });
        this.openProjectLink.setOnMouseEntered(event -> {
            openProjectLink.setTextFill(Paint.valueOf("#e28d38"));
            openProjectLink.setCursor(Cursor.HAND);
        });
        this.openProjectLink.setOnMouseExited(event -> {
            openProjectLink.setTextFill(Paint.valueOf("#3c3c3c"));
        });
        this.createProjectLink.setOnMouseClicked(event -> createNewProject());
        this.openProjectLink.setOnMouseClicked(event -> openProject());

        Label projectName = new Label();
        projectName.setTextFill(Paint.valueOf("#bcbcbc"));
        projectName.setCursor(Cursor.HAND);
        System.out.println(recentList);
        for (String p : recentList) {
            projectName.setText(p);

        }
        if (recentList.size() == 0) {
            recentVBox.setPrefWidth(0);
            recentVBox.setVisible(false);
            menuPane.setPrefWidth(600);
            //subMenu.setAlignment(openProjectLink, Pos.CENTER_RIGHT);
            //subMenu.setAlignment(createProjectLink,);
            // openProject.alignmentProperty().set(Pos.CENTER);

            // openProjectLink.alignmentProperty().set(Pos.CENTER);

        }
        recentVBox.getChildren().addAll(projectName);

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
        JsonArray recent = projectsJson.get("recent").getAsJsonArray();
        for (int i = 0; i < recent.size(); i++) {
            String path = recent.get(i).getAsString();
            recentList.add(path);
        }
        return false;
    }

    //TODO: check if project structure is respected before opening
    private void openProject() {
        DirectoryChooser dc = new DirectoryChooser();
        File f = dc.showDialog(Drawable.globalStage);
        if (f == null) {
            return;
        }
        String path = f.getAbsolutePath();
        Drawable.projectPath = projectPath = path;
        updateCurrentProject();
        EveryWhereLoader.getInstance().showLoader(Drawable.globalStage);
        showHome();
    }

    private void createNewProject() {
        DirectoryChooser dc = new DirectoryChooser();
        File f = dc.showDialog(Drawable.globalStage);
        if (f == null) {
            return;
        }
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
                .runAsync(GlobalProjectGeneration.this::createprojectHierarchy)
                .thenAccept(aVoid -> {
                    updateCurrentProject();
                    showHome();
                });
    }

    private void updateCurrentProject() {
        try {
            JsonObject projectsJson = new JsonParser().parse(new FileReader("./src/main/projects.json"))
                    .getAsJsonObject();
            //set current
            projectsJson.addProperty("current", projectPath);
            //add to recent
            JsonArray newRecent = new JsonArray();
            newRecent.add(projectPath);
            for (String path : recentList) {
                if (!path.equals(projectPath)) {
                    newRecent.add(path);
                }
            }
            projectsJson.add("recent", newRecent);

            FileUtils.writeStringToFile(new File("./src/main/projects.json"), projectsJson.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> loadRecent() {
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
        JsonArray recent = projectsJson.get("recent").getAsJsonArray();

        for (int i = 0; i < recent.size(); i++) {
            String path = recent.get(i).getAsString();
            System.out.println(path);
            recentList.add(path);
        }
        return recentList;
    }

    private void showHome() {
        Drawable.globalStage.setMaximized(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/homeLayouts/HomeLayout.fxml"));
        try {
            EveryWhereLoader.getInstance().stopLoader(loader.load());
            System.out.println("Loader should be stopped");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createprojectHierarchy() {
        new File(projectPath).mkdir();
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

    private String dialogSplit() throws IOException {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Drawable Project Name");
        dialog.setHeaderText("Enter your project title");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(s -> splitn = s);
        return splitn;
    }


}
