package tn.disguisedtoast.drawable.settingsModule.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSExpression;
import com.helger.css.writer.CSSWriter;
import com.helger.css.writer.CSSWriterSettings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import tn.disguisedtoast.drawable.ProjectMain.Drawable;
import tn.disguisedtoast.drawable.models.GeneratedElement;
import tn.disguisedtoast.drawable.previewModule.controllers.PreviewController;
import tn.disguisedtoast.drawable.settingsModule.TestMain.Main;
import tn.disguisedtoast.drawable.settingsModule.interfaces.SettingsControllerInterface;
import tn.disguisedtoast.drawable.settingsModule.utils.CssRuleExtractor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class ImageSettingsViewController implements Initializable, SettingsControllerInterface {
    @FXML public TextField filePath;
    @FXML
    public SplitMenuButton browseButton;
    @FXML
    public MenuItem loggedUserPhotoButton;
    @FXML public Slider horizontalPosition;
    @FXML public Slider verticalPosition;

    @FXML
    private Slider horizontalScale;
    @FXML
    private Slider verticalScale;
    @FXML
    private Label posHValue;
    @FXML
    private Label posVValue;
    @FXML
    private Label scaleHValue;
    @FXML
    private Label scaleVValue;
    @FXML
    private Label deleteButton;
    @FXML
    private ComboBox fitComboBox;

    private final CSSWriter aWriter = new CSSWriter (new CSSWriterSettings(ECSSVersion.CSS30, false));

    private String generatedViewsPath;

    private GeneratedElement imageView;

    private String[] fitOptions = {"Cover", "Fill", "Contain", "None"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        aWriter.setContentCharset (StandardCharsets.UTF_8.name ());
        generatedViewsPath = (Drawable.projectPath + "&RelatedFiles").replace("&", File.separator);
        this.fitComboBox.getItems().addAll(fitOptions);

        this.fitComboBox.setOnAction(event -> {
            try {
                CSSDeclaration declaration = imageView.getCssRules().getDeclarationOfPropertyName("object-fit");
                imageView.getCssRules().removeDeclaration(declaration);
            } catch (NullPointerException e) {
                System.out.println(e);
            }
            imageView.getCssRules().addDeclaration(new CSSDeclaration("object-fit", CSSExpression.createSimple(this.fitComboBox.getValue().toString().toLowerCase())));
            String cssString = aWriter.getCSSAsString(imageView.getCssRules());
            imageView.getElement().attr("style", cssString);
            imageView.getDomElement().setAttribute("style", cssString);
        });

        this.deleteButton.setOnMouseClicked(event -> {
            this.imageView.getElement().remove();
            PreviewController.refresh();
            SettingsViewController.getInstance().clearSettingView();
        });

        browseButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Images", "*.*"),
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("PNG", "*.png")
            );
            File image = fileChooser.showOpenDialog(Main.globalStage);
            if(image != null && image.exists()) {
                try {
                    filePath.setText(image.getPath());
                    Path oldImagePath = Paths.get(generatedViewsPath + imageView.getElement().attr("src").substring(5));
                    String newImagePath = "assets/drawable/"+RandomStringUtils.randomAlphanumeric(8)+".png";
                    FileUtils.copyFile(new File(image.getPath()), new File(generatedViewsPath + "/" + newImagePath));
                    imageView.getElement().attr("src", "../../"+newImagePath);
                    imageView.getDomElement().setAttribute("src", "../../" + newImagePath);
                    if (!oldImagePath.getFileName().toString().equals("placeholder.png") && !oldImagePath.getFileName().toString().equals("facebook.png")) {
                        new File(oldImagePath.toUri()).delete();
                    }

                    String pageConfPath = SettingsViewController.pageFolder + File.separator + "conf.json";
                    JsonObject globalConfigJson = new JsonParser().parse(new FileReader(pageConfPath)).getAsJsonObject();
                    JsonObject bindingsObject;
                    if (globalConfigJson.has("bindings") && (bindingsObject = globalConfigJson.getAsJsonObject("bindings")).has(this.imageView.getElement().id())) {
                        bindingsObject.remove(this.imageView.getElement().id());
                        Files.write(Paths.get(pageConfPath), new Gson().toJson(globalConfigJson).getBytes());
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        setUpPosition();
        setUpScale();

        try {
            JsonObject globalConfigJson = new JsonParser().parse(new FileReader(Drawable.projectPath + File.separator + "state.json")).getAsJsonObject();
            if (globalConfigJson.has("firebase") && globalConfigJson.get("firebase").getAsJsonObject().get("platforms").getAsJsonObject().has("facebook")) {
                ((Label) this.loggedUserPhotoButton.getGraphic()).getGraphic().setDisable(false);
                ((Label) this.loggedUserPhotoButton.getGraphic()).setTooltip(null);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.loggedUserPhotoButton.setOnAction(event -> {
            if (!((Label) this.loggedUserPhotoButton.getGraphic()).getGraphic().isDisable()) {
                try {
                    String pageConfPath = SettingsViewController.pageFolder + File.separator + "conf.json";
                    JsonObject globalConfigJson = new JsonParser().parse(new FileReader(pageConfPath)).getAsJsonObject();

                    if (globalConfigJson.has("bindings")) {
                        JsonObject bindingsObject = globalConfigJson.getAsJsonObject("bindings");
                        bindingsObject.addProperty(this.imageView.getElement().id(), "image");
                    } else {
                        JsonObject bindingsObject = new JsonObject();
                        bindingsObject.addProperty(this.imageView.getElement().id(), "image");
                        globalConfigJson.add("bindings", bindingsObject);
                    }
                    Files.write(Paths.get(pageConfPath), new Gson().toJson(globalConfigJson).getBytes());

                    Path oldImagePath = Paths.get(generatedViewsPath + imageView.getElement().attr("src").substring(5));
                    if (!oldImagePath.getFileName().toString().equals("placeholder.png") && !oldImagePath.getFileName().toString().equals("facebook.png")) {
                        new File(oldImagePath.toUri()).delete();
                    }

                    this.filePath.setText("Image bound to logged user photo.");
                    this.imageView.getElement().attr("src", "../../assets/facebook.png");
                    this.imageView.getDomElement().setAttribute("src", "../../assets/facebook.png");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setUpPosition() {
        horizontalPosition.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                CSSDeclaration declaration = imageView.getCssRules().getDeclarationOfPropertyName("left");
                imageView.getCssRules().removeDeclaration(declaration);
            }catch (NullPointerException e) {
                System.out.println(e);
            }
            double value = Math.round(newValue.doubleValue() * 10) / 10.0;
            imageView.getCssRules().addDeclaration(new CSSDeclaration("left", CSSExpression.createSimple(value + "%")));
            this.posHValue.setText("(" + value + "%)");
            String cssString = aWriter.getCSSAsString (imageView.getCssRules());
            imageView.getElement().attr("style", cssString);
            imageView.getDomElement().setAttribute("style", cssString);
        });

        verticalPosition.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                CSSDeclaration declaration = imageView.getCssRules().getDeclarationOfPropertyName("top");
                imageView.getCssRules().removeDeclaration(declaration);
            }catch (NullPointerException e) {
                System.out.println(e);
            }
            double value = Math.round(newValue.doubleValue() * 10) / 10.0;
            imageView.getCssRules().addDeclaration(new CSSDeclaration("top", CSSExpression.createSimple(value + "%")));
            this.posVValue.setText("(" + value + "%)");

            String cssString = aWriter.getCSSAsString (imageView.getCssRules());
            imageView.getElement().attr("style", cssString);
            imageView.getDomElement().setAttribute("style", cssString);
        });
    }

    private void setUpScale() {
        horizontalScale.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                CSSDeclaration declaration = imageView.getCssRules().getDeclarationOfPropertyName("width");
                imageView.getCssRules().removeDeclaration(declaration);
            } catch (NullPointerException e) {
                System.out.println(e);
            }
            double value = Math.round(newValue.doubleValue() * 10) / 10.0;
            if (value > 0) {
                imageView.getCssRules().addDeclaration(new CSSDeclaration("width", CSSExpression.createSimple(value + "%")));
                this.scaleHValue.setText("(" + value + "%)");
            } else {
                this.scaleHValue.setText("(Default)");
            }
            String cssRules = aWriter.getCSSAsString(imageView.getCssRules());
            imageView.getElement().attr("style", cssRules);
            imageView.getDomElement().setAttribute("style", cssRules);
        });

        verticalScale.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                CSSDeclaration declaration = imageView.getCssRules().getDeclarationOfPropertyName("height");
                imageView.getCssRules().removeDeclaration(declaration);
            } catch (NullPointerException e) {
                System.out.println(e);
            }
            double value = Math.round(newValue.doubleValue() * 10) / 10.0;
            if (value > 0) {
                imageView.getCssRules().addDeclaration(new CSSDeclaration("height", CSSExpression.createSimple(value + "%")));
                this.scaleVValue.setText("(" + value + "%)");
            } else {
                this.scaleVValue.setText("(Default)");
            }
            String cssRules = aWriter.getCSSAsString(imageView.getCssRules());
            imageView.getElement().attr("style", cssRules);
            imageView.getDomElement().setAttribute("style", cssRules);
        });
    }

    public void setImageView(GeneratedElement imageView){
        this.imageView = imageView;
        filePath.setText(generatedViewsPath + this.imageView.getElement().attr("src").substring(5));

        if (this.imageView.getElement().attr("src").contains("facebook")) {
            this.filePath.setText("Image bound to logged user photo.");
        }

        this.fitComboBox.setValue(StringUtils.capitalize(CssRuleExtractor.extractValue(this.imageView.getCssRules(), "object-fit")));

        setImagePosition();
        setImageScale();
    }

    private void setImagePosition() {
        try {
            String horizontalString = CssRuleExtractor.extractValue(imageView.getCssRules(), "left");
            double hPos = Double.parseDouble(horizontalString.substring(0,horizontalString.length()-1));
            this.horizontalPosition.setValue(hPos);
        }catch (Exception e){
            e.printStackTrace();
            this.horizontalPosition.setValue(0);
        }

        try {
            String verticalString = CssRuleExtractor.extractValue(imageView.getCssRules(), "top");
            double vPos = Double.parseDouble(verticalString.substring(0, verticalString.length()-1));
            this.verticalPosition.setValue(vPos);
        }catch (Exception e){
            this.verticalPosition.setValue(0);
        }
    }


    private void setImageScale() {
        try {
            String horizontalString = CssRuleExtractor.extractValue(imageView.getCssRules(), "width");
            double hPos = Double.parseDouble(horizontalString.substring(0, horizontalString.length() - 1));
            this.horizontalScale.setValue(hPos);
        } catch (Exception e) {
            this.horizontalScale.setValue(0.1);
            this.horizontalScale.setValue(0);
        }

        try {
            String verticalString = CssRuleExtractor.extractValue(imageView.getCssRules(), "height");
            double vPos = Double.parseDouble(verticalString.substring(0, verticalString.length() - 1));
            this.verticalScale.setValue(vPos);
        } catch (Exception e) {
            this.verticalScale.setValue(0.1);
            this.verticalScale.setValue(0);
        }
    }

    @Override
    public void save() {
        PreviewController.saveDocument();
    }
}
