package preview;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class AppViewController {

    @FXML
    private Pane cameraPane;
    @FXML
    private Pane previewPane;

    public Pane getCameraPane() {
        return cameraPane;
    }

    public void setCameraPane(Pane cameraPane) {
        this.cameraPane = cameraPane;
    }

    public Pane getPreviewPane() {
        return previewPane;
    }

    public void setPreviewPane(Pane previewPane) {
        this.previewPane = previewPane;
    }
}
