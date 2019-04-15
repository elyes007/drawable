package tn.disguisedtoast.drawable.projectGenerationModule.generation;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.io.FileUtils;

import java.io.File;
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
    public static Stage Stage;
    public static String splitn;
    public static String projectPath;
    @FXML
    public BorderPane startPane;
    @FXML
    public StackPane overPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.newProject.setOnMouseClicked(event -> {


            DirectoryChooser dc = new DirectoryChooser();
            //dc.showDialog(primaryStage);
            File f = dc.showDialog(Stage);
            String s = f.getAbsolutePath();
            System.out.println(s);
            try {
                splitn = dialogSplit();
                System.out.println(splitn);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("hello");
            final ProgressIndicator progress = new ProgressIndicator();
            progress.setMaxSize(50, 50);
            startPane.setCenter(progress);
            projectPath = s + "\\" + splitn;
            saveglobalPath(projectPath);

            CompletableFuture task = CompletableFuture
                    .runAsync(GlobalProjectGeneration::createprojectHierarchy)
                    .thenAccept(aVoid -> {
                        startPane.setCenter(null);
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/homeLayouts/HomeLayout.fxml"));
                        try {
                            startPane.setCenter(loader.load());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

            /*Task task = new Task<Void>() {
                @Override
                public Void call() {
                    ProjectGeneration.generateBlankProject(Stage,projectPath);
                    return null;
                }
            };*/
        });
        this.openProject.setOnMouseClicked(event -> {
            final ProgressIndicator progress = new ProgressIndicator();
            progress.setMaxSize(50, 50);
            startPane.setCenter(progress);

            DirectoryChooser dc = new DirectoryChooser();
            //dc.showDialog(primaryStage);
            File f = dc.showDialog(Stage);
            String s = f.getAbsolutePath();
            System.out.println(s);
            saveglobalPath(s);
            //HomeController.pagesPath =projectPath+"\\RelatedFiles\\pages";

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/homeLayouts/HomeLayout.fxml"));
            try {
               startPane.setCenter(loader.load());
               /* Stage.setScene(new Scene(startPane));
                Stage.setMaximized(true);
                Stage.show();*/
            } catch (IOException e) {
                e.printStackTrace();
            }
          /*  try {
                splitn = dialogSplit();
                System.out.println(splitn);
            } catch (IOException e) {
                e.printStackTrace();
            }
            projectPath = s + "\\" + splitn;*/

           /* Timeline timeline=new Timeline();
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(2),

            timeline.play();*/




        });
    }
    public static String saveglobalPath(String path){
        return path;
    }
    public static void loadHome(String fxml) {
       // FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/homeLayouts/PageCellView.fxml"));
     // BorderPane  startPane = loader.load();
    }

    public static void createprojectHierarchy() {
        new File(projectPath).mkdir();
        new File(projectPath + "\\RelatedFiles").mkdir();
        new File(projectPath + "\\RelatedFiles\\previewModule").mkdir();
        new File(projectPath + "\\RelatedFiles\\assets").mkdir();
        new File(projectPath + "\\RelatedFiles\\pages").mkdir();
        try {
            FileUtils.copyDirectory(new File(System.getProperty("user.dir") + "\\src\\main\\RelatedFiles\\previewModule"),
                    new File(projectPath + "\\RelatedFiles\\previewModule"));
            FileUtils.copyToDirectory(new File(System.getProperty("user.dir") + "\\src\\main\\RelatedFiles\\generated_views\\assets\\drawable\\placeholder.png"),new File(projectPath + "\\RelatedFiles\\assets"));
            System.out.println("Done!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void openProject(String projectPath){
        //Stage = primaryStage;
        Parent root = null;
        try {
            root = (new FXMLLoader(GlobalProjectGeneration.class.getResource("/layouts/homeLayouts/HomeLayout.fxml"))).load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage.setTitle("Drawable");
        Stage.setScene(new Scene(root));
        Stage.setMaximized(true);
        Stage.show();
        //height = primaryStage.getScene().getHeight();
       // width = primaryStage.getScene().getWidth();
        //primaryStage.setWidth(1366);
        //primaryStage.setHeight(768);
        Stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });






    }


    public static String dialogSplit() throws IOException {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Drawable Project Name");
        dialog.setHeaderText("Enter your project title");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            splitn = result.get();

        }
        return splitn;
    }


}
