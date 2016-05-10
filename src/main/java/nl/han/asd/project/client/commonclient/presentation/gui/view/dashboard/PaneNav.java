package nl.han.asd.project.client.commonclient.presentation.gui.view.dashboard;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.presentation.gui.view.Pane;
import nl.han.asd.project.client.commonclient.presentation.gui.view.PaneDashboard;

import static nl.han.asd.project.client.commonclient.presentation.gui.view.Pane.fancyLabel;

/**
 * Created by Marius on 26-04-16.
 */
public class PaneNav {
    HBox hBox = null;
    private PaneDashboard paneDashboard;

    public PaneNav(PaneDashboard paneDashboard) {
        this.paneDashboard = paneDashboard;

        hBox = Pane.getHBox(10, new int[] { 5, 5, 5, 5 }, "");
        hBox.setStyle("-fx-background-color: #EEE; -fx-background: #EEE;");
        Label logoutBtn = new Label("Uitloggen");
        Label settingsBtn = new Label("Instellingen");

        hBox.getChildren().addAll(logoutBtn, settingsBtn);

        fancyLabel(logoutBtn, paneDashboard.getGUI());
        logoutBtn.setOnMouseClicked(e -> paneDashboard.getGUI().setStage(GUI.Page.LOGIN));

        fancyLabel(settingsBtn, paneDashboard.getGUI());
        settingsBtn.setOnMouseClicked(e -> paneDashboard.getGUI().setStage(GUI.Page.SETTINGS));
    }

    public Node getHBox() {
        return hBox;
    }
}
