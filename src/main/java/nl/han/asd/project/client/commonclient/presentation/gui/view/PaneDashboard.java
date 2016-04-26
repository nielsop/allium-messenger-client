package nl.han.asd.project.client.commonclient.presentation.gui.view;

import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.presentation.gui.view.dashboard.PaneChat;
import nl.han.asd.project.client.commonclient.presentation.gui.view.dashboard.PaneContacts;

/**
 * Created by Marius on 25-04-16.
 */
public class PaneDashboard extends Pane {
    BorderPane borderPane = null;

    public PaneDashboard(GUI gui) {
        borderPane = getBorderPane(new int[]{0, 0, 0, 0});
        borderPane.setLeft(new PaneContacts(gui).getScrollPane());
        borderPane.setCenter(new PaneChat(gui).getScrollPane());
    }

    public Parent getBorderPane() {
        return borderPane;
    }
}
