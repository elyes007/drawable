package tn.disguisedtoast.drawable.projectGenerationModule.generation;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.DirectoryChooser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import tn.disguisedtoast.drawable.ProjectMain.Drawable;
import tn.disguisedtoast.drawable.utils.EveryWhereLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
    public VBox mainVbox;
    @FXML
    public BorderPane subMenu;


    private String splitn;
    private String projectPath;
    private String packageName;
    private static List<String> recentList = new ArrayList<>();
    private static List<String> results;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (checkCurrentProject()) return;
        this.newProject.setOnMouseClicked(event -> {
            try {
                createNewProject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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
        this.createProjectLink.setOnMouseClicked(event -> {
            try {
                createNewProject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        this.openProjectLink.setOnMouseClicked(event -> openProject());

        /*Label projectName = new Label();
        projectName.setTextFill(Paint.valueOf("#bcbcbc"));
        projectName.setCursor(Cursor.HAND);
        System.out.println(recentList);*/
        deleteRecentProject();
        for (String p : recentList) {
            Label projectName = new Label();
            projectName.setTextFill(Paint.valueOf("#bcbcbc"));
            projectName.setCursor(Cursor.HAND);
            projectName.setAlignment(Pos.CENTER_LEFT);
            System.out.println(p);
            projectName.setText(p);
            recentVBox.getChildren().addAll(projectName);
            projectName.setOnMouseClicked(event -> {
                openRecentProject(p);
            });
            projectName.setOnMouseEntered(event -> projectName.setTextFill(Paint.valueOf("#e28d38")));
            projectName.setOnMouseExited(event -> projectName.setTextFill(Paint.valueOf("#d9d9db")));
        }
        if (recentList.size() == 0) {
            recentVBox.setPrefWidth(0);
            recentVBox.setVisible(false);
            mainVbox.setPrefWidth(600);
            mainVbox.setAlignment(Pos.CENTER);

            //subMenu.setAlignment(openProjectLink, Pos.CENTER_RIGHT);
            //subMenu.setAlignment(createProjectLink,);
            // openProject.alignmentProperty().set(Pos.CENTER);

            // openProjectLink.alignmentProperty().set(Pos.CENTER);

        }
    }

    public void openRecentProject(String path) {
        Drawable.projectPath = projectPath = path;
        updateCurrentProject();
        EveryWhereLoader.getInstance().showLoader(Drawable.globalStage);
        showHome();
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


    private void createNewProject() throws IOException {
        DirectoryChooser dc = new DirectoryChooser();
        File f = dc.showDialog(Drawable.globalStage);
        if (f == null) {
            return;
        }
        String s = f.getAbsolutePath();
        System.out.println(s);
        Results results = dialogSplit();
        if (results == null) return;
        projectPath = s + File.separator + results.projectName;
        if (results.projectName.isEmpty() || results.pkgName.isEmpty() || (results.projectName.isEmpty() && results.pkgName.isEmpty()))
            return;
        Drawable.projectPath = projectPath;

        EveryWhereLoader.getInstance().showLoader(Drawable.globalStage);
        CompletableFuture
                .runAsync(GlobalProjectGeneration.this::createprojectHierarchy)
                .thenAccept(aVoid -> {
                    updateCurrentProject();
                    createStateFile(results.pkgName);
                    Platform.runLater(this::showHome);
                });
    }


    private void createStateFile(String pkgName) {
        try {
            String filePath = Drawable.projectPath + File.separator + "state.json";
            String content = "{\"ionic_state\":false, \"package_name\":\"" + pkgName + "\"}";
            FileUtils.write(new File(filePath), content);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void deleteRecentProject() {

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
            if (!new File(path).exists()) {
                System.out.println(path);
                StringUtils.remove(path, path);
                recentList.remove(path);
                System.out.println("path deleted");
            }
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/GlobalView.fxml"));
        try {
            EveryWhereLoader.getInstance().stopLoaderAndRefresh(loader.load(), null);
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
            FileUtils.copyToDirectory(new File(System.getProperty("user.dir") + "\\src\\main\\RelatedFiles\\generated_views\\assets\\drawable\\facebook.png"), new File(projectPath + "\\RelatedFiles\\assets"));
            FileUtils.copyToDirectory(new File(getClass().getResource("/storyboardModule/storyboard.html").getPath()), new File(projectPath + "\\RelatedFiles"));
            FileUtils.writeStringToFile(new File(Drawable.projectPath + "&RelatedFiles&storyboard.json".replace("&", File.separator)),
                    "{\"zoom\":70,\"pages\":[]}");
            FileUtils.writeStringToFile(new File(Drawable.projectPath + File.separator + "state.json"),
                    "{\n\t\"ionic_state\":false\n}");
            System.out.println("Done!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Results dialogSplit() throws IOException {
        Dialog<Results> dialog = new Dialog<>();
        dialog.setTitle("Dialog Test");
        dialog.setHeaderText("Please specify…");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Label projectLabel = new Label("your Project Name:");
        TextField projectName = new TextField();
        Label packageLabel = new Label("your package Name:");
        TextField pkgName = new TextField();
        dialogPane.setContent(new VBox(8, projectLabel, projectName, packageLabel, pkgName));

        //Platform.runLater(projectName::requestFocus);
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                System.out.println(pkgName.getText());
                if (pkgName.getText().equals("") || projectName.getText().equals("") || (projectName.getText().equals("") && pkgName.getText().equals(""))) {
                    new Alert(Alert.AlertType.WARNING, "Please set your project", ButtonType.CLOSE).show();
                }

                return new Results(projectName.getText(), pkgName.getText());
            }
            return null;
        });


        try {
            return dialog.showAndWait().get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    private static class Results {

        String projectName;
        String pkgName;


        public Results(String name, String pkgName) {
            this.projectName = name;
            this.pkgName = pkgName;

        }
    }

}
