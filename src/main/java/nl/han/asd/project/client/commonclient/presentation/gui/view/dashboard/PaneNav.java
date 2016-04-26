package nl.han.asd.project.client.commonclient.presentation.gui.view.dashboard;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.presentation.gui.view.Pane;

/**
 * Created by Marius on 26-04-16.
 */
public class PaneNav {
    HBox hBox = null;

    public PaneNav(GUI gui) {
        hBox = Pane.getHBox(10, new int[]{0, 0, 0, 0}, "");
        Label logoutBtn = new Label("Uitloggen");
        logoutBtn.setTextFill(Paint.valueOf("#888"));
        Label settingsBtn = new Label("Instellingen");
        settingsBtn.setTextFill(Paint.valueOf("#888"));

        hBox.getChildren().addAll(logoutBtn, settingsBtn);

        logoutBtn.setOnMouseEntered(e -> {
            logoutBtn.setTextFill(Paint.valueOf("#000"));
            gui.getScene().setCursor(Cursor.HAND);
        });
        logoutBtn.setOnMouseExited(e -> {
            logoutBtn.setTextFill(Paint.valueOf("#888"));
            gui.getScene().setCursor(Cursor.DEFAULT);
        });
        logoutBtn.setOnMouseClicked(e -> gui.setStage(GUI.Page.LOGIN));

        settingsBtn.setOnMouseEntered(e -> {
            settingsBtn.setTextFill(Paint.valueOf("#000"));
            gui.getScene().setCursor(Cursor.HAND);
        });
        settingsBtn.setOnMouseExited(e -> {
            settingsBtn.setTextFill(Paint.valueOf("#888"));
            gui.getScene().setCursor(Cursor.DEFAULT);
        });
        settingsBtn.setOnMouseClicked(e -> gui.setStage(GUI.Page.SETTINGS));
    }

    public Node gethBox() {
        return hBox;
    }
}
