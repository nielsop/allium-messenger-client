package nl.han.asd.project.client.commonclient.presentation.gui.view.dashboard;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import nl.han.asd.project.client.commonclient.presentation.gui.PaneFactory;

/**
 * Created by Marius on 26-04-16.
 */
public class NavView {
    private HBox hBox;
    private Label logout;
    private Label settings;

    public NavView() {
        hBox = PaneFactory.getHBox(10, new int[] { 5, 5, 5, 5 }, "");
        hBox.setStyle("-fx-background-color: #EEE; -fx-background: #EEE;");
        logout = new Label("Uitloggen");
        settings = new Label("Instellingen");

        hBox.getChildren().addAll(logout, settings);
    }

    public HBox getHBox() {
        return hBox;
    }

    public Label getLogout() {
        return logout;
    }

    public Label getSettings() {
        return settings;
    }
}
