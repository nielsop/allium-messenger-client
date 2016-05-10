package nl.han.asd.project.client.commonclient.presentation.gui.controller;

import javafx.scene.layout.GridPane;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.presentation.gui.model.ConfirmationModel;
import nl.han.asd.project.client.commonclient.presentation.gui.view.ConfirmationView;

/**
 * Created by Marius on 25-04-16.
 */
public class ConfirmationController {
    private ConfirmationModel model;
    private ConfirmationView view;

    public ConfirmationController(GUI gui) {
        model = new ConfirmationModel(gui);
        view = new ConfirmationView();
    }

    public GridPane getGridPane() {
        return view.getGridPane();
    }
}
