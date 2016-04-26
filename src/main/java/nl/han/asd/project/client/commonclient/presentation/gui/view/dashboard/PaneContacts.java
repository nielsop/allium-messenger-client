package nl.han.asd.project.client.commonclient.presentation.gui.view.dashboard;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.presentation.gui.view.Pane;

/**
 * Created by Marius on 25-04-16.
 */
public class PaneContacts extends Pane {
    ScrollPane scrollPane = null;

    public PaneContacts(GUI gui) {
        String style = "-fx-background-color:transparent; -fx-background: #DDD;";
        scrollPane = getScrollPane(true, false, new int[]{100, 300}, null, style);
        scrollPane.setContent(getContent());
    }

    private VBox getContent() {
        String style = "-fx-background-color: transparent;";
        VBox vBox = getVBox(10, new int[]{10, 10, 10, 10}, style);
        vBox.getChildren().add(new Label("contacten"));
        return vBox;
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }
}
