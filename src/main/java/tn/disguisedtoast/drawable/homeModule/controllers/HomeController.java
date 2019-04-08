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
import tn.disguisedtoast.drawable.ProjectMain.Drawable;
import tn.disguisedtoast.drawable.detectionModule.controllers.CamChooserController;
import tn.disguisedtoast.drawable.detectionModule.controllers.CamStreamViewController;
import tn.disguisedtoast.drawable.homeModule.models.Page;
import tn.disguisedtoast.drawable.settingsModule.controllers.SettingsViewController;
import tn.disguisedtoast.drawable.utils.EveryWhereLoader;

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
    public AnchorPane addButtonPane;
    @FXML
    private HBox pagesPreviewHBox;

    public static Stage primaryStage;
    private List<PageCellViewController> pageCellViewControllers;
    private String pagesPath = System.getProperty("user.dir") + "\\src\\main\\RelatedFiles\\generated_views\\pages";

    private PageCellViewController.PageClickCallback pageClickCallback = page -> {
        try {
            EveryWhereLoader.getInstance().showLoader(Drawable.globalStage);
            FXMLLoader loader = new FXMLLoader(SettingsViewController.class.getResource("/layouts/settingsViews/SettingsView.fxml"));
            EveryWhereLoader.getInstance().stopLoader(loader.load());
            SettingsViewController controller = loader.getController();
            controller.init(page.getFolderName());
        } catch (IOException e) {
            e.printStackTrace();
            EveryWhereLoader.getInstance().stopLoader(null);
        }
    };
    private Stage chooserStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ((Button) this.addButtonPane.getChildren().get(0)).setOnAction(event -> {
            EveryWhereLoader.getInstance().showLoader(Drawable.globalStage);
            chooserStage = new Stage();
            chooserStage.setTitle("Camera Chooser");
            chooserStage.setScene(new Scene(new CamChooserController(this).getRoot()));
            chooserStage.setHeight(200);
            chooserStage.setWidth(500);
            chooserStage.setResizable(false);
            chooserStage.centerOnScreen();
            chooserStage.show();
            EveryWhereLoader.getInstance().stopLoader(null);
        });

        refresh();

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
    }

    @Override
    public void onButtonClicked(int webcamIndex) {
        chooserStage.close();
        if (webcamIndex != -1) {
            try {
                EveryWhereLoader.getInstance().showLoader(Drawable.globalStage);
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/layouts/detectionViews/CamStreamView.fxml"));
                loader.getLocation().openStream();
                EveryWhereLoader.getInstance().stopLoader(loader.load());
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

    private List<Page> loadPages() {
        File root = new File(System.getProperty("user.dir") + "\\src\\main\\RelatedFiles\\generated_views\\pages");
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

    public void refresh() {
        pagesPreviewHBox.getChildren().clear();
        pagesPreviewHBox.getChildren().add(this.addButtonPane);
        pageCellViewControllers = new ArrayList<>();
        List<Page> pages = loadPages();
        for (Page page : pages) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/homeLayouts/PageCellView.fxml"));
                Pane pagePane = loader.load();
                PageCellViewController pageCellViewController = loader.getController();
                pageCellViewController.setPage(page, pageClickCallback, this);
                pageCellViewControllers.add(pageCellViewController);
                pagesPreviewHBox.getChildren().addAll(pagePane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
