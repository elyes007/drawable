package tn.disguisedtoast.drawable.settingsModule.utils;

import com.sun.javafx.scene.control.skin.ColorPalette;
import com.sun.javafx.scene.control.skin.ColorPickerSkin;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

@SuppressWarnings("restriction")
public class CustomColorPicker extends ColorPicker {

    private Label lbl;

    final static LinearGradient RED_LINE = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.WHITE), new Stop(0.45, Color.WHITE),
            new Stop(0.46, Color.RED), new Stop(0.54, Color.RED),
            new Stop(0.55, Color.WHITE), new Stop(1, Color.WHITE));

    @Override
    protected Skin<?> createDefaultSkin() {

        final CustomColorPickerSkin skin = new CustomColorPickerSkin(this);
        lbl = (Label)skin.getDisplayNode();
        final StackPane pane = (StackPane)lbl.getGraphic();
        final Rectangle rect = (Rectangle)pane.getChildren().get(0);

        // set initial color to red line if transparent is shown
        if (getValue().equals(Color.TRANSPARENT)){
            rect.setFill(RED_LINE);
            lbl.setText("Default");
        }

        // set color to red line when transparent is selected
        rect.fillProperty().addListener((o, oldVal, newVal) -> {
            if (newVal != null && newVal.equals(Color.TRANSPARENT)){
                rect.setFill(RED_LINE);
                lbl.setText("Default");
            }
        });

        return skin;
    }

    private class CustomColorPickerSkin extends ColorPickerSkin {

        private boolean initialized = false;

        public CustomColorPickerSkin(ColorPicker colorPicker) {
            super(colorPicker);
        }

        @Override
        protected void handleControlPropertyChanged(String p) {
            super.handleControlPropertyChanged(p);
            if(getValue() == Color.TRANSPARENT) {
                lbl.setText("Default");
            }
        }

        @Override
        protected Node getPopupContent() {
            final ColorPalette popupContent = (ColorPalette)super.getPopupContent();

            // make sure listeners and geometry are only created once
            if (!initialized) {
                final VBox paletteBox = (VBox)popupContent.getChildrenUnmodifiable().get(0);
                final StackPane hoverSquare = (StackPane)popupContent.getChildrenUnmodifiable().get(1); // ColorSquare
                final Rectangle hoverRect = (Rectangle)hoverSquare.getChildren().get(0); // ColorSquare
                final GridPane grid = (GridPane)paletteBox.getChildren().get(0); // ColorPalette
                final StackPane colorSquare = (StackPane)grid.getChildren().get(grid.getChildren().size()-1); // ColorSquare
                final Rectangle colorRect = (Rectangle)colorSquare.getChildren().get(0);

                // set fill color of original color rectangle to transparent
                // (can't be set to red line gradient because ComboBoxBase<Color> tries to cast it to Color)
                colorRect.setFill(Color.TRANSPARENT);
                // put another rectangle with red line on top of it
                colorSquare.getChildren().add(new Rectangle(colorRect.getWidth(), colorRect.getHeight(), RED_LINE));
                // show red line gradient also in hover rectangle when the transparent color is selected
                hoverRect.fillProperty().addListener((o, oldVal, newVal) -> {
                    if (newVal.equals(Color.TRANSPARENT)){
                        hoverRect.setFill(RED_LINE);
                        lbl.setText("Default");
                    }
                });
                initialized = true;
            }
            return popupContent;
        }
    }

}
