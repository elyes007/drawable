package tn.disguisedtoast.drawable.settingsModule.controllers;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSExpression;
import com.helger.css.writer.CSSWriter;
import com.helger.css.writer.CSSWriterSettings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import tn.disguisedtoast.drawable.ProjectMain.Drawable;
import tn.disguisedtoast.drawable.models.GeneratedElement;
import tn.disguisedtoast.drawable.previewModule.controllers.PreviewController;
import tn.disguisedtoast.drawable.settingsModule.TestMain.Main;
import tn.disguisedtoast.drawable.settingsModule.interfaces.SettingsControllerInterface;
import tn.disguisedtoast.drawable.settingsModule.utils.CssRuleExtractor;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class ImageSettingsViewController implements Initializable, SettingsControllerInterface {
    @FXML public TextField filePath;
    @FXML public Button browseButton;
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

    private final CSSWriter aWriter = new CSSWriter (new CSSWriterSettings(ECSSVersion.CSS30, false));

    private String generatedViewsPath;

    private GeneratedElement imageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        aWriter.setContentCharset (StandardCharsets.UTF_8.name ());
        generatedViewsPath = (Drawable.projectPath + "&RelatedFiles").replace("&", File.separator);

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
                    if(!oldImagePath.getFileName().toString().equals("placeholder.png")){
                        new File(oldImagePath.toUri()).delete();
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        setUpPosition();
        setUpScale();
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
