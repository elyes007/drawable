package tn.disguisedtoast.drawable.settingsModule.controllers.globalSettings;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclarationList;
import com.helger.css.reader.CSSReaderDeclarationList;
import com.helger.css.writer.CSSWriter;
import com.helger.css.writer.CSSWriterSettings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import tn.disguisedtoast.drawable.ProjectMain.Drawable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class ThemeSettingsViewController implements Initializable {

    @FXML
    private Label baseColorPicker;
    @FXML
    private Label contrastColorPicker;
    @FXML
    private Label shadeColorPicker;
    @FXML
    private Label tintColorPicker;

    private String themeName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Theme Settings");
    }

    public void setTheme(String themeName) {
        this.themeName = themeName;

        try {
            String css = new String(Files.readAllBytes(Paths.get((Drawable.projectPath + "&RelatedFiles&themes").replace("&", File.separator)))).replace(":root {", "").replace("}", "");
            System.out.println(css);
            CSSDeclarationList cssDeclarations = CSSReaderDeclarationList.readFromString(css, ECSSVersion.CSS30);

            CSSWriter aWriter = new CSSWriter(new CSSWriterSettings(ECSSVersion.CSS30, false));
            System.out.println(":root {\n" + aWriter.getCSSAsString(cssDeclarations) + "\n}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
