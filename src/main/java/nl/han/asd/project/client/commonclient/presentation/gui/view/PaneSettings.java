package nl.han.asd.project.client.commonclient.presentation.gui.view;

import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;

/**
 * Created by Marius on 25-04-16.
 */
public class PaneSettings {
    GridPane gridPane = null;

    public PaneSettings(GUI gui) {
    }

    public Parent getGridPane() {
        return gridPane;
    }
}
