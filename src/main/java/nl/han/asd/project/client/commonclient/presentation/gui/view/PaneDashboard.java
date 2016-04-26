package nl.han.asd.project.client.commonclient.presentation.gui.view;

import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.presentation.gui.view.dashboard.PaneChat;
import nl.han.asd.project.client.commonclient.presentation.gui.view.dashboard.PaneContacts;
import nl.han.asd.project.client.commonclient.presentation.gui.view.dashboard.PaneNav;

/**
 * Created by Marius on 25-04-16.
 */
public class PaneDashboard {
    BorderPane borderPane = null;

    public PaneDashboard(GUI gui) {
        borderPane = Pane.getBorderPane(new int[]{0, 0, 0, 0});
        borderPane.setTop(new PaneNav(gui).gethBox());
        borderPane.setLeft(new PaneContacts(gui).getScrollPane());
        borderPane.setCenter(new PaneChat(gui).getScrollPane());
    }

    public Parent getBorderPane() {
        return borderPane;
    }
}
