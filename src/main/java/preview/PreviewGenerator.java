package preview;

import code_generation.entities.views.Button;
import code_generation.entities.views.EditText;
import code_generation.entities.views.ImageView;
import code_generation.entities.views.View;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PreviewGenerator {

    private static final double MAX_WIDTH = 400;
    private static final double MAX_HEIGHT = 600;

    public static List<Node> getNodes(List<View> views) {
        List<Node> nodes = new ArrayList<>();
        for (View view : views) {
            Node node = null;
            if (view instanceof Button) {
                node = new javafx.scene.control.Button();
                ((javafx.scene.control.Button) node).setText(((Button) view).getText());
                ((javafx.scene.control.Button) node).setPrefHeight(20);
                ((javafx.scene.control.Button) node).setPrefWidth(MAX_WIDTH * Double.parseDouble(view.getWidthPercent()));
            } else if (view instanceof EditText) {
                node = new TextField();
                ((TextField) node).setPromptText(((EditText) view).getHint());
                ((TextField) node).setPrefHeight(20);
                ((TextField) node).setPrefWidth(MAX_WIDTH * Double.parseDouble(view.getWidthPercent()));
            } else if (view instanceof ImageView) {
                node = new javafx.scene.image.ImageView();
                File file = new File("./placeholder.png");
                Image image = new Image(file.toURI().toString());
                ((javafx.scene.image.ImageView) node).setImage(image);
                ((javafx.scene.image.ImageView) node).setPreserveRatio(false);
                node.setPickOnBounds(true);
                ((javafx.scene.image.ImageView) node).setFitWidth(MAX_WIDTH * Double.parseDouble(view.getWidthPercent()));
                ((javafx.scene.image.ImageView) node).setFitHeight(MAX_HEIGHT * Double.parseDouble(view.getHeightPercent()));
                System.out.println("width: " + ((javafx.scene.image.ImageView) node).getFitWidth());
                System.out.println("height: " + ((javafx.scene.image.ImageView) node).getFitHeight());
            }

            double vBias = Double.parseDouble(view.getVerticalBias());
            double hBias = Double.parseDouble(view.getHorizontalBias());

            /*vbias = y / (totalheight - height)
                    = y / (totalheight - totalheight * heightPercent)
                    = y / (totalheight * (1 - height_percent))
            =>
            y = vbias * totalheight * (1 - height_percent)*/

            double y = vBias * MAX_HEIGHT * (1 - Double.parseDouble(view.getHeightPercent()));
            double x = hBias * MAX_WIDTH * (1 - Double.parseDouble(view.getWidthPercent()));

            node.setLayoutY(y);
            node.setLayoutX(x);
            node.setFocusTraversable(false);

            System.out.println("x: " + x + ", y: " + y);

            nodes.add(node);
        }

        return nodes;
    }
}
