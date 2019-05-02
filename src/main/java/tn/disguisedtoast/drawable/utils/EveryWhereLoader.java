package tn.disguisedtoast.drawable.utils;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URISyntaxException;

public class EveryWhereLoader {

    private static EveryWhereLoader instance;
    private Stage stage;
    private VBox vBox;

    private EveryWhereLoader() {
    }

    public static EveryWhereLoader getInstance() {
        if (instance == null) {
            instance = new EveryWhereLoader();
        }
        return instance;
    }

    public void showLoader(Stage stage) {
        Platform.runLater(() -> {
            try {
                this.stage = stage;
                vBox = new VBox();
                vBox.setAlignment(Pos.CENTER);

                ImageView imageView = new ImageView(new Image(getClass().getResource("/drawable/logo.png").toURI().toString()));
                imageView.setPreserveRatio(true);
                ImageViewPane imageViewPane = new ImageViewPane(imageView);

                double sideMargin = stage.getWidth() * 0.25;
                VBox.setMargin(imageViewPane, new Insets(0, sideMargin, 0, sideMargin));

                vBox.getChildren().add(imageViewPane);
                ProgressIndicator progressIndicator = new ProgressIndicator(ProgressIndicator.INDETERMINATE_PROGRESS);
                progressIndicator.setStyle(" -fx-progress-color: green;");

                vBox.getChildren().add(progressIndicator);

                vBox.setBackground(new Background(new BackgroundFill(Color.color(1, 1, 1), CornerRadii.EMPTY, Insets.EMPTY)));

                AnchorPane anchorPane = (AnchorPane) stage.getScene().getRoot();
                anchorPane.getChildren().add(vBox);

                AnchorPane.setTopAnchor(vBox, 0.0);
                AnchorPane.setRightAnchor(vBox, 0.0);
                AnchorPane.setBottomAnchor(vBox, 0.0);
                AnchorPane.setLeftAnchor(vBox, 0.0);

                FadeTransition ft = new FadeTransition(Duration.millis(500), vBox);
                ft.setFromValue(0.0);
                ft.setToValue(0.5);
                ft.play();

                //stage.getScene().setRoot(anchorPane);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });
    }

    public void stopLoader(Parent parent) {
        if (instance != null && instance.stage != null) {
            Platform.runLater(() -> {
                FadeTransition ft = new FadeTransition(Duration.millis(500), vBox);
                ft.setFromValue(0.5);
                ft.setToValue(0.0);
                ft.setOnFinished(event -> {
                    Parent root = instance.stage.getScene().getRoot();
                    System.out.println("Parent (in param):" + parent);
                    System.out.println("root: " + root);

                    AnchorPane anchorPane = (AnchorPane) instance.stage.getScene().getRoot();
                    anchorPane.getChildren().remove(1);

                    for (Node node : anchorPane.getChildren()) {
                        System.out.println(node.getClass());
                    }

                    if (parent != null) {
                        ((BorderPane) anchorPane.getChildren().get(0)).setCenter(parent);
                    }
                    instance = null;
                    System.gc();
                });
                ft.play();
            });
        }
    }

    public void stopLoaderAndRefresh(Parent parent, LoaderListener loaderListener) {
        if (instance != null) {
            Platform.runLater(() -> {
                FadeTransition ft = new FadeTransition(Duration.millis(500), vBox);
                ft.setFromValue(0.5);
                ft.setToValue(0.0);
                ft.setOnFinished(event -> {
                    Parent root = instance.stage.getScene().getRoot();
                    System.out.println("Parent (in param):" + parent);
                    System.out.println("root: " + root);

                    AnchorPane anchorPane = (AnchorPane) instance.stage.getScene().getRoot();
                    anchorPane.getChildren().remove(1);

                    if (parent != null) {
                        anchorPane.getChildren().remove(0);
                        anchorPane.getChildren().add(parent);

                        AnchorPane.setTopAnchor(parent, 0.0);
                        AnchorPane.setRightAnchor(parent, 0.0);
                        AnchorPane.setBottomAnchor(parent, 0.0);
                        AnchorPane.setLeftAnchor(parent, 0.0);
                    }
                    instance = null;
                    System.gc();
                    if (loaderListener != null)
                        loaderListener.finished();
                });
                ft.play();
            });
        }
    }

    public interface LoaderListener {
        void finished();
    }

}
