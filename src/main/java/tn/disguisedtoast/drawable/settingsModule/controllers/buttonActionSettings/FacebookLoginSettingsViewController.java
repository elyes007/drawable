package tn.disguisedtoast.drawable.settingsModule.controllers.buttonActionSettings;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import tn.disguisedtoast.drawable.ProjectMain.Drawable;
import tn.disguisedtoast.drawable.previewModule.controllers.PreviewController;
import tn.disguisedtoast.drawable.settingsModule.controllers.ButtonSettingsViewController;
import tn.disguisedtoast.drawable.settingsModule.controllers.SettingsViewController;
import tn.disguisedtoast.drawable.settingsModule.interfaces.SettingsControllerInterface;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class FacebookLoginSettingsViewController implements Initializable, SettingsControllerInterface {

    @FXML
    private TextField appName;
    @FXML
    private TextField appId;
    @FXML
    private TextField packageName;
    @FXML
    private TextField keyhash;
    @FXML
    private ComboBox destination;
    @FXML
    private Label appIdInfoLabel;
    @FXML
    private Label keyhashInfoLabel;
    @FXML
    private Hyperlink appIdInfoHyperlink;
    @FXML
    private Hyperlink keyhashInfoHyperlink;

    private JsonObject globalSettingsObject;
    private JsonObject facebookLogObject;
    private JsonObject buttonLogObject;
    private JsonObject pageSettingsObject;

    private Element element;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String pagesFolderPath = Drawable.projectPath + "&RelatedFiles&pages".replace("&", File.separator);
        System.out.println(pagesFolderPath);
        String[] directories = new File(pagesFolderPath).list((dir, name) -> (new File(dir, name).isDirectory() && !name.equals(Paths.get(SettingsViewController.pageFolder).getFileName().toString())) && !name.equals("temp"));

       /* List<String> pages = Arrays.stream(Objects.requireNonNull(new File(pagesFolderPath).listFiles((dir, name) -> (new File(dir, name).isDirectory() && !name.equals(Paths.get(SettingsViewController.pageFolder).getFileName().toString())) && !name.equals("temp")))).map(file1 -> {
            File[] files = file1.listFiles();
            if (files == null) return "";
            try {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].getName().equals("conf.json")) {
                        JsonObject pageConf = new JsonParser().parse(new FileReader(files[i])).getAsJsonObject();
                        return pageConf.get("page").getAsString();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return "";
        }).collect(Collectors.toList());*/

        destination.getItems().add("Nowhere");
        destination.getItems().addAll(directories);
        destination.getSelectionModel().select(0);

        this.appIdInfoLabel.setOnMouseClicked(event -> {
            Bounds boundsInScene = this.appIdInfoLabel.localToScene(this.appIdInfoLabel.getBoundsInLocal());
            this.appIdInfoLabel.getTooltip().show(this.appIdInfoLabel, boundsInScene.getMaxX(), boundsInScene.getMaxY() + this.appIdInfoLabel.getHeight());
            this.appIdInfoLabel.getTooltip().setAutoHide(true);
        });

        this.appIdInfoHyperlink.setOnAction(event -> {
            Drawable.getInstance().getHostServices().showDocument("https://ionicframework.com/docs/native/facebook#installation");
        });


        this.keyhashInfoLabel.setOnMouseClicked(event -> {
            Bounds boundsInScene = this.keyhashInfoLabel.localToScene(this.keyhashInfoLabel.getBoundsInLocal());
            this.keyhashInfoLabel.getTooltip().show(this.keyhashInfoLabel, boundsInScene.getMaxX(), boundsInScene.getMaxY() + this.keyhashInfoLabel.getHeight());
            this.keyhashInfoLabel.getTooltip().setAutoHide(true);
        });

        this.keyhashInfoHyperlink.setOnAction(event -> {
            Drawable.getInstance().getHostServices().showDocument("https://ionicframework.com/docs/native/facebook#installation");
        });

        try {
            String id = Jsoup.parse(new File(Drawable.projectPath + File.separator + "ionic_project" + File.separator + "config.xml"), "UTF-8").selectFirst("widget").attr("id");
            this.packageName.setText(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setElement(Element element, ButtonSettingsViewController buttonSettingsViewController) {
        this.element = element;
        getLogJsonObject();
        if (this.facebookLogObject != null && this.buttonLogObject != null) {
            this.appName.setText(this.facebookLogObject.get("appName").getAsString());
            this.appId.setText(this.facebookLogObject.get("appId").getAsString());
            String destination = this.buttonLogObject.get("destinationPageName").getAsString();
            if (destination.isEmpty()) {
                this.destination.getSelectionModel().select(0);
            } else {
                this.destination.setValue(this.buttonLogObject.get("destinationPageName").getAsString());
            }
        }

        Platform.runLater(() -> {
            ProcessBuilder processBuilder = new ProcessBuilder();
            String ionicToolsPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "RelatedFiles" + File.separator + "FirebaseLoginTools" + File.separator + "KeyHashTools";
            String keystorePath = System.getProperty("user.home") + "&.android&debug.keystore".replace("&", File.separator);
            if (!new File(keystorePath).exists()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Can't find android configuration", ButtonType.OK);
                alert.show();

                buttonSettingsViewController.buttonAction.getSelectionModel().select(0);
            }
            processBuilder.command("cmd.exe", "/c", "keytool -exportcert -alias androiddebugkey -keystore \"" + keystorePath + "\" -storepass android | \"" + ionicToolsPath + File.separator + "openssl\" sha1 -binary | \"" + ionicToolsPath + File.separator + "openssl\" base64");
            try {

                Process process = processBuilder.start();


                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                String line = reader.readLine();
                this.keyhash.setText(line);

                int exitCode = process.waitFor();

                System.out.println("\nExited with error code : " + exitCode);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        try {
            JsonObject globalConfig = new JsonParser().parse(new FileReader(Drawable.projectPath + File.separator + "state.json")).getAsJsonObject();
            if (!globalConfig.has("firebase")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "You need to configure firebase first!", ButtonType.OK, ButtonType.CANCEL);
                alert.setHeaderText("Firebase is not configured.");
                alert.showAndWait();

                if (alert.getResult() == ButtonType.OK) {
                    FirebaseConfigViewController.showPopup(buttonSettingsViewController);
                } else {
                    buttonSettingsViewController.buttonAction.getSelectionModel().select(0);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getLogJsonObject(){
        try {
            pageSettingsObject = new JsonParser().parse(new FileReader(SettingsViewController.pageFolder + "/conf.json")).getAsJsonObject();
            globalSettingsObject = new JsonParser().parse(new FileReader(Drawable.projectPath + File.separator + "state.json")).getAsJsonObject();

            if (pageSettingsObject.getAsJsonObject("actions").has(this.element.id())) {
                buttonLogObject = pageSettingsObject.getAsJsonObject("actions").getAsJsonObject(this.element.id());
                if (buttonLogObject.get("platform").getAsString().equals("facebook") && globalSettingsObject.has("firebase")) {
                    JsonObject firebasePlatObject = globalSettingsObject.getAsJsonObject("firebase").getAsJsonObject("platforms");
                    if (firebasePlatObject.has("facebook")) {
                        facebookLogObject = firebasePlatObject.getAsJsonObject("facebook");
                        this.appId.setText(facebookLogObject.get("appId").getAsString());
                        this.appName.setText(facebookLogObject.get("appName").getAsString());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        if (this.appId.getText().isEmpty() || this.appName.getText().isEmpty()) {
            return;
        }
        if (this.buttonLogObject != null) {
            if (!this.buttonLogObject.get("platform").equals("facebook")) {
                this.buttonLogObject.addProperty("platform", "facebook");
                this.buttonLogObject.addProperty("destinationPageName", (destination.getSelectionModel().getSelectedIndex() == 0 ? "" : destination.getValue().toString()));
            }
        } else {
            JsonObject newButtonLogObject = new JsonObject();
            newButtonLogObject.addProperty("platform", "facebook");
            System.out.println("desc: " + destination.getValue());
            newButtonLogObject.addProperty("destinationPageName", (destination.getSelectionModel().getSelectedIndex() == 0 ? "" : destination.getValue().toString()));

            this.pageSettingsObject.getAsJsonObject("actions").add(this.element.id(), newButtonLogObject);
            this.buttonLogObject = newButtonLogObject;
        }

        if (this.facebookLogObject != null) {
            System.out.println("facebook login not null");
            this.facebookLogObject.addProperty("appId", this.appId.getText());
            this.facebookLogObject.addProperty("appName", this.appName.getText());
        } else {
            JsonObject newFacebookLogObject = new JsonObject();
            newFacebookLogObject.addProperty("appId", this.appId.getText());
            newFacebookLogObject.addProperty("appName", this.appName.getText());

            try {
                this.globalSettingsObject = new JsonParser().parse(new FileReader(Drawable.projectPath + File.separator + "state.json")).getAsJsonObject();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            this.globalSettingsObject.getAsJsonObject("firebase").getAsJsonObject("platforms").add("facebook", newFacebookLogObject);
            System.out.println(this.globalSettingsObject);
            this.facebookLogObject = newFacebookLogObject;
        }

        try{
            Files.write(Paths.get(SettingsViewController.pageFolder + File.separator + "conf.json"), new Gson().toJson(pageSettingsObject).getBytes());
            Files.write(Paths.get(Drawable.projectPath + File.separator + "state.json"), new Gson().toJson(globalSettingsObject).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.element.attr("(click)", "logFacebook" + this.element.id() + "()");
        PreviewController.saveDocument();
    }
}
