package nl.han.asd.project.client.commonclient.presentation.gui.controller;

import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.presentation.gui.model.SettingModel;
import nl.han.asd.project.client.commonclient.presentation.gui.view.SettingView;

/**
 * Created by Marius on 25-04-16.
 */
public class SettingController {
    private SettingModel model;
    private SettingView view;

    public SettingController(GUI gui) {
        model = new SettingModel(gui);
        view = new SettingView();
    }

    public GridPane getGridPane() {
        return view.getGridPane();
    }
}
