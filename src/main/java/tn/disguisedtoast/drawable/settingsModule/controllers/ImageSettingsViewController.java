package tn.disguisedtoast.drawable.settingsModule.controllers;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSExpression;
import com.helger.css.writer.CSSWriter;
import com.helger.css.writer.CSSWriterSettings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import tn.disguisedtoast.drawable.models.GeneratedElement;
import tn.disguisedtoast.drawable.settingsModule.TestMain.Main;
import tn.disguisedtoast.drawable.settingsModule.utils.CssRuleExtractor;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class ImageSettingsViewController implements Initializable {
    @FXML public TextField filePath;
    @FXML public Button browseButton;
    @FXML public Slider horizontalPosition;
    @FXML public Slider verticalPosition;

    private final CSSWriter aWriter = new CSSWriter (new CSSWriterSettings(ECSSVersion.CSS30, false));

    private String generatedViewsPath;

    private GeneratedElement imageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        aWriter.setContentCharset (StandardCharsets.UTF_8.name ());
        generatedViewsPath = System.getProperty("user.dir")+"/src/main/RelatedFiles/generated_views";

        browseButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Images", "*.*"),
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("PNG", "*.png")
            );
            File image = fileChooser.showOpenDialog(Main.globalStage);
            if(image != null) {
                filePath.setText(image.getPath());
            }
        });

        filePath.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.equals(oldValue)) {
                    try {
                        String path = generatedViewsPath + "/" + newValue;
                        System.out.println(path);
                        File file = new File(path);
                        if (!file.exists()) {
                            String oldImagePath = imageView.getElement().attr("src");
                            if(!oldImagePath.equals("assets/drawable/placeholder.png")){
                                new File(generatedViewsPath + "/" + oldImagePath).delete();
                            }
                            String newImagePath = "assets/drawable/"+RandomStringUtils.randomAlphanumeric(8)+".png";
                            FileUtils.copyFile(new File(newValue), new File(generatedViewsPath + "/" + newImagePath));
                            imageView.getElement().attr("src", newImagePath);
                            imageView.getDomElement().setAttribute("src", newImagePath);
                        }
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        setUpPosition();
    }

    private void setUpPosition() {
        horizontalPosition.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                CSSDeclaration declaration = imageView.getCssRules().getDeclarationOfPropertyName("left");
                imageView.getCssRules().removeDeclaration(declaration);
            }catch (NullPointerException e) {
                System.out.println(e);
            }
            imageView.getCssRules().addDeclaration(new CSSDeclaration("left", CSSExpression.createSimple(newValue+"%")));
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
            imageView.getCssRules().addDeclaration(new CSSDeclaration("top", CSSExpression.createSimple(newValue+"%")));
            String cssString = aWriter.getCSSAsString (imageView.getCssRules());
            imageView.getElement().attr("style", cssString);
            imageView.getDomElement().setAttribute("style", cssString);
        });
    }

    public void setImageView(GeneratedElement imageView){
        this.imageView = imageView;
        filePath.setText(this.imageView.getElement().attr("src"));

        setImagePosition();
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
}
