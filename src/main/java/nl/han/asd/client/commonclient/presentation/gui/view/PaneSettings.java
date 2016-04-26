package nl.han.asd.client.commonclient.presentation.gui.view;

import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import nl.han.asd.client.commonclient.presentation.gui.GUI;

/**
 * Created by Marius on 25-04-16.
 */
public class PaneSettings extends Pane {
    GridPane pane = null;

    public PaneSettings(GUI gui) {
    }

    public Parent getPane() {
        return pane;
    }
}
