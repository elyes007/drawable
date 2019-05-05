package tn.disguisedtoast.drawable.settingsModule.controllers.globalSettings;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclarationList;
import com.helger.css.decl.CSSExpression;
import com.helger.css.reader.CSSReaderDeclarationList;
import com.helger.css.writer.CSSWriter;
import com.helger.css.writer.CSSWriterSettings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import tn.disguisedtoast.drawable.ProjectMain.Drawable;
import tn.disguisedtoast.drawable.settingsModule.interfaces.SettingsControllerInterface;
import tn.disguisedtoast.drawable.settingsModule.utils.CustomColorPicker;
import tn.disguisedtoast.drawable.settingsModule.utils.FxUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class ThemeSettingsViewController implements Initializable, SettingsControllerInterface {

    @FXML
    private Label baseColorPicker;
    @FXML
    private Label contrastColorPicker;
    @FXML
    private Label shadeColorPicker;
    @FXML
    private Label tintColorPicker;

    private String themeName;

    private CSSDeclarationList cssDeclarations;

    private CSSWriter aWriter = new CSSWriter(new CSSWriterSettings(ECSSVersion.CSS30, false));
    private Path themesPath;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.baseColorPicker.setGraphic(new CustomColorPicker());
        this.contrastColorPicker.setGraphic(new CustomColorPicker());
        this.shadeColorPicker.setGraphic(new CustomColorPicker());
        this.tintColorPicker.setGraphic(new CustomColorPicker());

        themesPath = Paths.get((Drawable.projectPath + "&RelatedFiles&themes").replace("&", File.separator));

        ((CustomColorPicker) this.baseColorPicker.getGraphic()).setOnAction(event -> {
            this.cssDeclarations.getDeclarationOfPropertyName("--ion-color-" + themeName).setExpression(CSSExpression.createSimple(FxUtils.toRGBCode(((CustomColorPicker) event.getSource()).getValue())));
        });
        ((CustomColorPicker) this.contrastColorPicker.getGraphic()).setOnAction(event -> {
            this.cssDeclarations.getDeclarationOfPropertyName("--ion-color-" + themeName + "-contrast").setExpression(CSSExpression.createSimple(FxUtils.toRGBCode(((CustomColorPicker) event.getSource()).getValue())));
        });
        ((CustomColorPicker) this.shadeColorPicker.getGraphic()).setOnAction(event -> {
            this.cssDeclarations.getDeclarationOfPropertyName("--ion-color-" + themeName + "-shade").setExpression(CSSExpression.createSimple(FxUtils.toRGBCode(((CustomColorPicker) event.getSource()).getValue())));
        });
        ((CustomColorPicker) this.tintColorPicker.getGraphic()).setOnAction(event -> {
            this.cssDeclarations.getDeclarationOfPropertyName("--ion-color-" + themeName + "-tint").setExpression(CSSExpression.createSimple(FxUtils.toRGBCode(((CustomColorPicker) event.getSource()).getValue())));
        });
    }

    public void setTheme(String themeName) {
        this.themeName = themeName;

        try {
            String css = new String(Files.readAllBytes(themesPath)).replace(":root {", "").replace("}", "");

            cssDeclarations = CSSReaderDeclarationList.readFromString(css, ECSSVersion.CSS30);

            ((CustomColorPicker) this.baseColorPicker.getGraphic()).setValue(Color.valueOf(cssDeclarations.getDeclarationOfPropertyName("--ion-color-" + themeName).getExpressionAsCSSString()));
            ((CustomColorPicker) this.contrastColorPicker.getGraphic()).setValue(Color.valueOf(cssDeclarations.getDeclarationOfPropertyName("--ion-color-" + themeName + "-contrast").getExpressionAsCSSString()));
            ((CustomColorPicker) this.shadeColorPicker.getGraphic()).setValue(Color.valueOf(cssDeclarations.getDeclarationOfPropertyName("--ion-color-" + themeName + "-shade").getExpressionAsCSSString()));
            ((CustomColorPicker) this.tintColorPicker.getGraphic()).setValue(Color.valueOf(cssDeclarations.getDeclarationOfPropertyName("--ion-color-" + themeName + "-tint").getExpressionAsCSSString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        try {
            System.out.println("Saved");
            Files.write(themesPath, aWriter.getCSSAsString(cssDeclarations).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
