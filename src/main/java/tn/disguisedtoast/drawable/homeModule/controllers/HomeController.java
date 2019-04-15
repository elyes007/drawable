package tn.disguisedtoast.drawable.homeModule.controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import tn.disguisedtoast.drawable.detectionModule.controllers.CamChooserController;
import tn.disguisedtoast.drawable.detectionModule.controllers.CamStreamViewController;
import tn.disguisedtoast.drawable.homeModule.models.Page;
import tn.disguisedtoast.drawable.settingsModule.controllers.SettingsViewController;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class HomeController implements CamChooserController.CameraButtonCallback, Initializable {
    @FXML
    public TextField search;
    @FXML
    public Button export;
    @FXML
    private AnchorPane addButtonPane;
    @FXML
    private HBox pagesPreviewHBox;

    public static Stage primaryStage;
    private List<PageCellViewController> pageCellViewControllers;
    public static String pagesPath = System.getProperty("user.dir") + "\\src\\main\\RelatedFiles\\generated_views\\pages";

    private PageCellViewController.PageClickCallback pageClickCallback = page -> {
        SettingsViewController.showStage(page.getFolderName());
    };
    private Stage chooserStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ((Button) this.addButtonPane.getChildren().get(0)).setOnAction(event -> {
            System.out.println("new");
            chooserStage = new Stage();
            chooserStage.setTitle("Camera Chooser");
            chooserStage.setScene(new Scene(new CamChooserController(this).getRoot()));
            chooserStage.setHeight(200);
            chooserStage.setWidth(500);
            chooserStage.setResizable(false);
            chooserStage.centerOnScreen();
            chooserStage.show();
        });

        //Loading pages
        pageCellViewControllers = new ArrayList<>();
        List<Page> pages = loadPages();
        for (Page page : pages) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/homeLayouts/PageCellView.fxml"));
                Pane pagePane = loader.load();
                PageCellViewController pageCellViewController = loader.getController();
                pageCellViewController.setPage(page, pageClickCallback);
                pageCellViewControllers.add(pageCellViewController);

                pagesPreviewHBox.getChildren().addAll(pagePane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.search.setOnKeyReleased(event -> {
            if (this.search.getText().isEmpty()) {
                Platform.runLater(() -> {
                    this.pagesPreviewHBox.getChildren().clear();
                    this.pagesPreviewHBox.getChildren().add(addButtonPane);
                    this.pagesPreviewHBox.getChildren().addAll(
                            this.pageCellViewControllers.stream().map(
                                    pageCellViewController -> pageCellViewController.getPagePane()
                            ).collect(Collectors.toList())
                    );
                });
            } else {
                Platform.runLater(() -> {
                    this.pagesPreviewHBox.getChildren().clear();
                    this.pagesPreviewHBox.getChildren().addAll(
                            this.pageCellViewControllers.stream().filter(
                                    pageCellViewController -> pageCellViewController.getPage().getName().contains(this.search.getText())
                            ).map(
                                    pageCellViewController -> pageCellViewController.getPagePane()
                            ).collect(Collectors.toList())
                    );
                });
            }
        });

        /*pages = new ArrayList<>();
        imageList.setCellFactory(new PageCellFactory());
        loadPages();
        imageList.getItems().addAll(pages);

        this.search.setOnKeyReleased(event -> {
            List<Page> subPages = pages.stream().filter(page -> page.getName().contains(this.search.getText())).collect(Collectors.toList());
            System.out.println(subPages);
            Platform.runLater(() -> {
                imageList.getItems().clear();
                imageList.getItems().addAll(subPages);
            });
        });*/
    }

    @Override
    public void onButtonClicked(int webcamIndex) {
        chooserStage.close();
        if (webcamIndex != -1) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/layouts/detectionViews/CamStreamView.fxml"));
                loader.load();
                loader.getLocation().openStream();
                CamStreamViewController controller = loader.getController();
                controller.init(webcamIndex);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

   @FXML
   public void exportProject(ActionEvent event) {
        // String command ="cmd /c ionic start newProject";
        // Runtime rt = Runtime.getRuntime();
        /*ProcessBuilder processBuilder = new ProcessBuilder();
        // Windows
        processBuilder.command("cmd.exe", "/c", "ping -n 3 google.com");
        try {
           // rt.exec(new String[]{"cmd.exe","/c","ionic "});
            Process process = Runtime.getRuntime().exec(command);
            Scanner kb = new Scanner(process.getInputStream());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/

        File files = new File(pagesPath );

        // File folder = new File();
        String[] entries = files.list();
        for (String s : entries) {
            File currentFile = new File(files.getPath(), s);
            System.out.println(currentFile.length());
        }


        // Windows

        Platform.runLater(() -> {
            DirectoryChooser dc = new DirectoryChooser();
            //dc.showDialog(primaryStage);
            File f = dc.showDialog(primaryStage);
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

    public List<Page> loadPages() {
        File root = new File(pagesPath);
        String[] directories = root.list((dir, name) -> (new File(dir, name).isDirectory()));
        List<Page> pages = new ArrayList<>();
        for (String dir : directories) {
            if (dir.equals("temp")) continue;
            try {
                JsonObject jsonObject = new JsonParser().parse(new FileReader(pagesPath + "/" + dir + "/conf.json")).getAsJsonObject();
                String pageName = jsonObject.get("page").getAsString();
                Page pg = new Page(pageName, pagesPath + "/" + dir);
                pages.add(pg);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return pages;
    }


}
