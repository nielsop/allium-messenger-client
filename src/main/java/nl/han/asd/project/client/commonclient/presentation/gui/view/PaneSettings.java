package nl.han.asd.project.client.commonclient.presentation.gui.view;

import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;

/**
 * Created by Marius on 25-04-16.
 */
public class PaneSettings {
    GridPane gridPane = null;
    private GUI gui;

    public PaneSettings(GUI gui) {
        this.gui = gui;
    }

    public Parent getGridPane() {
        return gridPane;
    }
}
