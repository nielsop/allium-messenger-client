package nl.han.asd.project.client.commonclient.presentation.gui.view.dashboard;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.presentation.gui.view.Pane;

/**
 * Created by Marius on 26-04-16.
 */
public class PaneNav {
    BorderPane borderPane = null;

    public PaneNav(GUI gui) {
        borderPane = Pane.getBorderPane(new int[]{0,0,0,0});
        Button logoutBtn = new Button("Uitloggen");
        Button settingsBtn = new Button("Instellingen");
        borderPane.setLeft(settingsBtn);
        borderPane.setRight(logoutBtn);
        borderPane.setStyle("-fx-background-color: darkgreen");

        logoutBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                gui.setStage(GUI.Page.LOGIN);
            }
        });
        settingsBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                gui.setStage(GUI.Page.SETTINGS);
            }
        });
    }

    public Node getBorderPane() {
        return borderPane;
    }
}
