package tn.disguisedtoast.drawable.settingsModule.utils;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import tn.disguisedtoast.drawable.settingsModule.models.IonIcon;

public class IconComboboxCell extends ListCell<IonIcon> {
    private final ImageView view;

    public IconComboboxCell() {
        //setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        view = new ImageView();
    }

    @Override protected void updateItem(IonIcon item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
            setGraphic(null);
        } else {
            view.setImage(item.getImage());
            setText(item.getName());
            setTextFill(Color.WHITE);
            setGraphic(view);
        }
    }
}
