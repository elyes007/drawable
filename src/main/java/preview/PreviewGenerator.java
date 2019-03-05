package preview;

import code_generation.entities.views.Button;
import code_generation.entities.views.EditText;
import code_generation.entities.views.ImageView;
import code_generation.entities.views.View;
import code_generation.service.CodeGenerator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import org.eclipse.jgit.diff.Edit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PreviewGenerator {

    private static double MAX_WIDTH;
    private static double MAX_HEIGHT;
    private static final double BUTTON_HEIGHT = 20;
    private static final double TEXTFIELD_HEIGHT = 20;
    private static final double MARGIN_TOP = 15;

    public static List<Node> getNodes(CodeGenerator.ParseResult parseResult, double width, double height) {
        MAX_WIDTH = width;
        MAX_HEIGHT = height;
        if (parseResult.hasSingleFrame()) {
            return getFromSingleFrame(parseResult.getRows());
        } else {
            return getFromDoubleFrames(parseResult.getViews());
        }
    }

    private static List<Node> getFromDoubleFrames(List<View> views) {
        List<Node> nodes = new ArrayList<>();
        for (View view : views) {
            Node node = null;
            if (view instanceof Button) {
                node = new javafx.scene.control.Button();
                ((javafx.scene.control.Button) node).setText(((Button) view).getText());
                ((javafx.scene.control.Button) node).setPrefHeight(BUTTON_HEIGHT);
                ((javafx.scene.control.Button) node).setPrefWidth(MAX_WIDTH * Double.parseDouble(view.getWidthPercent()));
                node.getStyleClass().add("preview_button");
                node.setFocusTraversable(false);
            } else if (view instanceof EditText) {
                node = new TextField();
                ((TextField) node).setPromptText(((EditText) view).getHint());
                ((TextField) node).setPrefHeight(TEXTFIELD_HEIGHT);
                ((TextField) node).setPrefWidth(MAX_WIDTH * Double.parseDouble(view.getWidthPercent()));
                ((TextField) node).setEditable(false);
                node.getStyleClass().add("preview_edit_text");
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

            double y = vBias * MAX_HEIGHT * (1 - (view.getHeightPercent() != null ? Double.parseDouble(view.getHeightPercent()) : 0.03));
            double x = hBias * MAX_WIDTH * (1 - Double.parseDouble(view.getWidthPercent()));

            node.setLayoutY(y);
            node.setLayoutX(x);
            node.setFocusTraversable(false);

            System.out.println("x: " + x + ", y: " + y);

            nodes.add(node);
        }

        return nodes;
    }

    private static List<Node> getFromSingleFrame(List<List<CodeGenerator.ObjectWrapper>> rows) {
        List<Node> nodes = new ArrayList<>();
        double y = MARGIN_TOP, dy = 0;
        for (List<CodeGenerator.ObjectWrapper> row : rows) {
            y += dy;
            dy = 0;
            for (CodeGenerator.ObjectWrapper wrapper : row) {
                View view = wrapper.getView();
                Node node = null;
                if (view instanceof Button) {
                    node = new javafx.scene.control.Button();
                    ((javafx.scene.control.Button) node).setText(((Button) view).getText());
                    ((javafx.scene.control.Button) node).setPrefHeight(BUTTON_HEIGHT);
                    dy = dy < BUTTON_HEIGHT ? BUTTON_HEIGHT + MARGIN_TOP : dy;
                    ((javafx.scene.control.Button) node).setPrefWidth(MAX_WIDTH * Double.parseDouble(view.getWidthPercent()));
                } else if (view instanceof EditText) {
                    node = new TextField();
                    ((TextField) node).setPromptText(((EditText) view).getHint());
                    ((TextField) node).setPrefHeight(TEXTFIELD_HEIGHT);
                    dy = dy < TEXTFIELD_HEIGHT ? TEXTFIELD_HEIGHT + MARGIN_TOP : dy;
                    ((TextField) node).setPrefWidth(MAX_WIDTH * Double.parseDouble(view.getWidthPercent()));
                } else if (view instanceof ImageView) {
                    node = new javafx.scene.image.ImageView();
                    File file = new File("./placeholder.png");
                    Image image = new Image(file.toURI().toString());
                    ((javafx.scene.image.ImageView) node).setImage(image);
                    ((javafx.scene.image.ImageView) node).setPreserveRatio(false);
                    node.setPickOnBounds(true);
                    double side = MAX_WIDTH * Double.parseDouble(view.getWidthPercent());
                    ((javafx.scene.image.ImageView) node).setFitWidth(side);
                    ((javafx.scene.image.ImageView) node).setFitHeight(side);
                    dy = dy < side ? side + MARGIN_TOP : dy;
                }

                double hBias = Double.parseDouble(view.getHorizontalBias());
                double x = hBias * MAX_WIDTH * (1 - Double.parseDouble(view.getWidthPercent()));

                node.setLayoutY(y);
                node.setLayoutX(x);
                node.setFocusTraversable(false);

                nodes.add(node);
            }
        }

        return nodes;
    }
}
