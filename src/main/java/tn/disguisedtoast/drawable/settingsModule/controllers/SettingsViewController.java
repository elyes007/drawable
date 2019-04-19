package tn.disguisedtoast.drawable.settingsModule.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import tn.disguisedtoast.drawable.ProjectMain.Drawable;
import tn.disguisedtoast.drawable.models.GeneratedElement;
import tn.disguisedtoast.drawable.models.SupportedComponents;
import tn.disguisedtoast.drawable.previewModule.controllers.PreviewController;
import tn.disguisedtoast.drawable.settingsModule.controllers.menuSettings.MenuBarSettingController;
import tn.disguisedtoast.drawable.settingsModule.controllers.menuSettings.MenuButtonSettingsController;
import tn.disguisedtoast.drawable.settingsModule.controllers.menuSettings.MenuSettingsController;
import tn.disguisedtoast.drawable.settingsModule.interfaces.SettingsControllerInterface;
import tn.disguisedtoast.drawable.utils.EveryWhereLoader;
import tn.disguisedtoast.drawable.utils.JsonUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsViewController implements Initializable {
    @FXML private AnchorPane settingsPane;
    @FXML private Pane previewPane;
    @FXML private Button saveButton;
    @FXML private Button finishButton;
    @FXML
    private Button cancelButton;

    private SettingsControllerInterface currentController;
    public static String pageFolder;

    private static SettingsViewController instance;

    public void init(String pageFolder) {
        SettingsViewController.pageFolder = pageFolder;

        String htmlPath = JsonUtils.getValue("html", pageFolder + "/conf.json");
        String path = pageFolder + "/" + htmlPath;
        Node previewRoot = PreviewController.getView(path, element -> setComponent(element));
        previewPane.getChildren().add(previewRoot);
    }

    public static SettingsViewController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;

        this.saveButton.setDisable(true);
        this.saveButton.setOnAction(event -> currentController.save());
        this.finishButton.setOnAction(event -> {
            EveryWhereLoader.getInstance().showLoader(Drawable.globalStage);
            PreviewController.saveSnapshot(pageFolder + File.separator + "snapshot.png", () -> {
                try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/homeLayouts/HomeLayout.fxml"));
                    EveryWhereLoader.getInstance().stopLoader(loader.load());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
        this.cancelButton.setOnAction(event -> {
            try {
                EveryWhereLoader.getInstance().showLoader(Drawable.globalStage);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/homeLayouts/HomeLayout.fxml"));
                EveryWhereLoader.getInstance().stopLoader(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void clearSettingView() {
        settingsPane.getChildren().clear();
    }

    public void setComponent(GeneratedElement element){
        settingsPane.getChildren().clear();
        try{
            System.out.println("Yoo: " + element.getElement().tagName());
            if(element.getElement().tagName().equals(SupportedComponents.ION_BUTTON.toString())){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/ButtonSettingsView.fxml"));
                Pane pane = loader.load();
                AnchorPane.setTopAnchor(pane, 0.0);
                AnchorPane.setLeftAnchor(pane, 0.0);
                AnchorPane.setRightAnchor(pane, 0.0);

                settingsPane.getChildren().add(pane);
                currentController = loader.getController();
                ((ButtonSettingsViewController) currentController).setButton(element);
            }else if(element.getElement().tagName().equals(SupportedComponents.ION_IMG.toString())){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/ImageSettingsView.fxml"));
                Pane pane = loader.load();
                AnchorPane.setTopAnchor(pane, 0.0);
                AnchorPane.setLeftAnchor(pane, 0.0);
                AnchorPane.setRightAnchor(pane, 0.0);
                settingsPane.getChildren().add(pane);
                currentController = loader.getController();
                ((ImageSettingsViewController)currentController).setImageView(element);
            } else if(element.getElement().tagName().equals(SupportedComponents.ION_ITEM.toString()) && element.getElement().select(SupportedComponents.ION_INPUT.toString()).size() != 0){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/EditTextSettingsView.fxml"));
                Pane pane = loader.load();
                AnchorPane.setTopAnchor(pane, 0.0);
                AnchorPane.setLeftAnchor(pane, 0.0);
                AnchorPane.setRightAnchor(pane, 0.0);
                settingsPane.getChildren().add(pane);
                currentController = loader.getController();
                ((EditTextSettingsViewController)currentController).setTextField(element);
            } else if(element.getElement().tagName().equals(SupportedComponents.ION_LABEL.toString())) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/LabelSettingsView.fxml"));
                Pane pane = loader.load();
                AnchorPane.setTopAnchor(pane, 0.0);
                AnchorPane.setLeftAnchor(pane, 0.0);
                AnchorPane.setRightAnchor(pane, 0.0);
                settingsPane.getChildren().add(pane);
                currentController = loader.getController();
                ((LabelSettingsViewController)currentController).setLabel(element);
            } else if (element.getElement().tagName().equals(SupportedComponents.ION_TOOLBAR.toString()) && element.getElement().id().equals("menu_toolbar")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/menuSettings/MenuBarSettingsView.fxml"));
                Pane pane = loader.load();
                AnchorPane.setTopAnchor(pane, 0.0);
                AnchorPane.setLeftAnchor(pane, 0.0);
                AnchorPane.setRightAnchor(pane, 0.0);
                settingsPane.getChildren().add(pane);
                currentController = loader.getController();
                ((MenuBarSettingController) currentController).setMenuBarElement(element);
            } else if(element.getElement().tagName().equals(SupportedComponents.ION_TOOLBAR.toString())) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/ToolbarSettingsView.fxml"));
                Pane pane = loader.load();
                AnchorPane.setTopAnchor(pane, 0.0);
                AnchorPane.setLeftAnchor(pane, 0.0);
                AnchorPane.setRightAnchor(pane, 0.0);
                settingsPane.getChildren().add(pane);
                currentController = loader.getController();
                ((ToolbarSettingsViewController)currentController).setToolbarElement(element);
            } else if(element.getElement().tagName().equals(SupportedComponents.BODY.toString())) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/GlobalPageSettingsView.fxml"));
                Pane pane = loader.load();
                AnchorPane.setTopAnchor(pane, 0.0);
                AnchorPane.setLeftAnchor(pane, 0.0);
                AnchorPane.setRightAnchor(pane, 0.0);
                settingsPane.getChildren().add(pane);
                currentController = loader.getController();
                ((GlobalPageSettingsViewController)currentController).setBodyGeneratedElement(element);
            } else if (element.getElement().tagName().equals(SupportedComponents.ION_ITEM.toString()) && element.getElement().classNames().contains("menu_item")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/menuSettings/MenuButtonSettingsView.fxml"));
                Pane pane = loader.load();
                AnchorPane.setTopAnchor(pane, 0.0);
                AnchorPane.setLeftAnchor(pane, 0.0);
                AnchorPane.setRightAnchor(pane, 0.0);

                settingsPane.getChildren().add(pane);
                currentController = loader.getController();
                ((MenuButtonSettingsController) currentController).setMenuButton(element);
            } else if (element.getElement().tagName().equals(SupportedComponents.ION_CONTENT.toString())) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/menuSettings/MenuSettingsView.fxml"));
                Pane pane = loader.load();
                AnchorPane.setTopAnchor(pane, 0.0);
                AnchorPane.setLeftAnchor(pane, 0.0);
                AnchorPane.setRightAnchor(pane, 0.0);

                settingsPane.getChildren().add(pane);
                currentController = loader.getController();
                ((MenuSettingsController) currentController).setIonContentElement(element);
            } else if (element.getElement().tagName().equals(SupportedComponents.ION_TAB_BUTTON.toString())) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/TabSettingsView.fxml"));
                Pane pane = loader.load();
                AnchorPane.setTopAnchor(pane, 0.0);
                AnchorPane.setLeftAnchor(pane, 0.0);
                AnchorPane.setRightAnchor(pane, 0.0);

                settingsPane.getChildren().add(pane);
                currentController = loader.getController();
                ((TabSettingsController) currentController).setTabElement(element);
            }

            //HomeController.primaryStage.sizeToScene();
            if(currentController == null) {
                this.saveButton.setDisable(true);
            }else{
                this.saveButton.setDisable(false);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
