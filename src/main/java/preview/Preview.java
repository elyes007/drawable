package preview;

import code_generation.entities.views.Button;
import code_generation.entities.views.EditText;
import code_generation.entities.views.ImageView;
import code_generation.entities.views.View;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Preview extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Preview");
        AnchorPane root = new AnchorPane();
        root.setPrefHeight(600);
        root.setPrefWidth(400);

        Button button = new Button();
        button.setText("Button1");
        button.setHorizontalBias("0");
        button.setVerticalBias("0.1");
        button.setWidthPercent("0.2");
        button.setHeightPercent("0.1");

        EditText editText = new EditText();
        editText.setHint("Edit text 1");
        editText.setHorizontalBias("0.5");
        editText.setVerticalBias("0.1");
        editText.setWidthPercent("0.4");
        editText.setHeightPercent("0.1");

        ImageView imageView = new ImageView();
        imageView.setHorizontalBias("0.5");
        imageView.setVerticalBias("0.5");
        imageView.setWidthPercent("0.5");
        imageView.setHeightPercent("0.5");

        List<View> views = new ArrayList<>();
        views.add(button);
        views.add(editText);
        views.add(imageView);

        root.getChildren().addAll(PreviewGenerator.getNodes(views));

        primaryStage.setScene(new Scene(root));
        primaryStage.setWidth(400);
        primaryStage.setHeight(600);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
