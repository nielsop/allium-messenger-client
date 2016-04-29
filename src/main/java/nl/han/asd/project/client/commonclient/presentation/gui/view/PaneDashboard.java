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
    private final PaneNav paneNav;
    private final PaneContacts paneContacts;
    private final PaneChat paneChat;
    BorderPane borderPane = null;

    public PaneDashboard(GUI gui) {
        paneNav = new PaneNav(gui, this);
        paneContacts = new PaneContacts(gui, this);
        paneChat = new PaneChat(gui, this);

        borderPane = Pane.getBorderPane(new int[]{0, 0, 0, 0});
        borderPane.setTop(paneNav.getHBox());
        borderPane.setLeft(paneContacts.getScrollPane());
        borderPane.setCenter(paneChat.getBorderPane());
    }

    public PaneNav getPaneNav() {
        return paneNav;
    }

    public PaneContacts getPaneContacts() {
        return paneContacts;
    }

    public PaneChat getPaneChat() {
        return paneChat;
    }

    public Parent getBorderPane() {
        return borderPane;
    }
}
