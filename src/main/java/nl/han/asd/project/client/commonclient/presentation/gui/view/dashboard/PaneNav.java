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
        hBox = Pane.getHBox(10, new int[]{5, 5, 5, 5}, "");
        Label logoutBtn = new Label("Uitloggen");
        logoutBtn.setTextFill(Paint.valueOf("#888"));
        Label settingsBtn = new Label("Instellingen");
        settingsBtn.setTextFill(Paint.valueOf("#888"));

        hBox.getChildren().addAll(logoutBtn, settingsBtn);

        fancyLabel(logoutBtn, gui);
        logoutBtn.setOnMouseClicked(e -> gui.setStage(GUI.Page.LOGIN));

        fancyLabel(settingsBtn, gui);
        settingsBtn.setOnMouseClicked(e -> gui.setStage(GUI.Page.SETTINGS));
    }

    private void fancyLabel(Label label, GUI gui) {
        label.setOnMouseEntered(e -> {
            label.setTextFill(Paint.valueOf("#000"));
            gui.getScene().setCursor(Cursor.HAND);
        });
        label.setOnMouseExited(e -> {
            label.setTextFill(Paint.valueOf("#888"));
            gui.getScene().setCursor(Cursor.DEFAULT);
        });
    }

    public Node gethBox() {
        return hBox;
    }
}
