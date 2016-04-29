package nl.han.asd.project.client.commonclient.presentation.gui.view.dashboard;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.presentation.gui.view.Pane;
import nl.han.asd.project.client.commonclient.presentation.gui.view.PaneDashboard;

/**
 * Created by Marius on 26-04-16.
 */
public class PaneNav {
    private final GUI gui;
    private final PaneDashboard paneDashboard;
    HBox hBox = null;

    public PaneNav(GUI gui, PaneDashboard paneDashboard) {
        this.gui = gui;
        this.paneDashboard = paneDashboard;
        setupPane(gui);
    }

    private void setupPane(GUI gui) {
        hBox = Pane.getHBox(10, new int[]{5, 5, 5, 5}, "");
        Label logoutBtn = new Label("Uitloggen");
        Label settingsBtn = new Label("Instellingen");

        hBox.getChildren().addAll(logoutBtn, settingsBtn);

        Pane.fancyLabel(logoutBtn, gui);
        logoutBtn.setOnMouseClicked(e -> gui.setStage(GUI.Page.LOGIN));

        Pane.fancyLabel(settingsBtn, gui);
        settingsBtn.setOnMouseClicked(e -> gui.setStage(GUI.Page.SETTINGS));
    }

    private void fancyLabel(Label label, GUI gui) {
        label.setTextFill(Paint.valueOf("#888"));
        label.setOnMouseEntered(e -> {
            label.setTextFill(Paint.valueOf("#000"));
            gui.getScene().setCursor(Cursor.HAND);
        });
        label.setOnMouseExited(e -> {
            label.setTextFill(Paint.valueOf("#888"));
            gui.getScene().setCursor(Cursor.DEFAULT);
        });
    }

    public Node getHBox() {
        return hBox;
    }
}
