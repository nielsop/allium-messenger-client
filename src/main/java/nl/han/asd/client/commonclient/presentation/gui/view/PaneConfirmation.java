package nl.han.asd.client.commonclient.presentation.gui.view;

import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import nl.han.asd.client.commonclient.presentation.gui.GUI;

/**
 * Created by Marius on 25-04-16.
 */
public class PaneConfirmation extends Pane {
    GridPane pane = null;

    public PaneConfirmation(GUI gui) {
    }

    public Parent getPane() {
        return pane;
    }
}
