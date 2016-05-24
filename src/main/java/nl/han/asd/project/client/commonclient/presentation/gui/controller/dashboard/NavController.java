package nl.han.asd.project.client.commonclient.presentation.gui.controller.dashboard;

import javafx.scene.layout.HBox;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.presentation.gui.controller.DashboardController;
import nl.han.asd.project.client.commonclient.presentation.gui.model.dashboard.NavModel;
import nl.han.asd.project.client.commonclient.presentation.gui.view.dashboard.NavView;

import static nl.han.asd.project.client.commonclient.presentation.gui.PaneFactory.fancyLabel;

/**
 * Created by Marius on 26-04-16.
 */
public class NavController {
    private NavModel model;
    private NavView view;

    public NavController(DashboardController dashboardController) {
        model = new NavModel(dashboardController);
        view = new NavView();
        onActions();
    }

    private void onActions() {
        fancyLabel(view.getLogout(), model.getGUI());
        view.getLogout().setOnMouseClicked(e -> model.getGUI().setScene(GUI.Page.LOGIN));

        fancyLabel(view.getLogout(), model.getGUI());
        view.getLogout().setOnMouseClicked(e -> model.getGUI().setScene(GUI.Page.SETTINGS));
    }

    public HBox getHBox() {
        return view.getHBox();
    }
}
