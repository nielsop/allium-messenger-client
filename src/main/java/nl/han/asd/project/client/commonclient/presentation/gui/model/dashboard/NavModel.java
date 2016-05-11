package nl.han.asd.project.client.commonclient.presentation.gui.model.dashboard;

import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.presentation.gui.controller.DashboardController;
import nl.han.asd.project.client.commonclient.presentation.gui.view.DashboardView;

/**
 * Created by Marius on 26-04-16.
 */
public class NavModel {
    private DashboardController dashboardController;

    public NavModel(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    public GUI getGUI() {
        return dashboardController.getGUI();
    }
}
