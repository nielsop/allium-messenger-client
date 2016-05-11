package nl.han.asd.project.client.commonclient.presentation.gui.view;

import javafx.scene.layout.BorderPane;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.presentation.gui.view.dashboard.PaneChat;
import nl.han.asd.project.client.commonclient.presentation.gui.view.dashboard.PaneContacts;
import nl.han.asd.project.client.commonclient.presentation.gui.view.dashboard.PaneNav;
import nl.han.asd.project.client.commonclient.store.Contact;

import java.util.ArrayList;

/**
 * Created by Marius on 25-04-16.
 */
public class PaneDashboard {
    private PaneNav paneNav;
    private PaneContacts paneContacts;
    private PaneChat paneChat;
    private BorderPane borderPane;
    private GUI gui;
    private Contact me;
    private ArrayList<Contact> contacts;

    public PaneDashboard(GUI gui) {
        this.gui = gui;
        me = gui.pLayer.getCurrentUser();

        paneNav = new PaneNav(this);
        paneContacts = new PaneContacts(this);
        paneChat = new PaneChat(this);

        borderPane = Pane.getBorderPane(new int[] { 0, 0, 0, 0 });
        borderPane.setStyle("-fx-background-color: #FFF; -fx-background: #FFF;");
        borderPane.setTop(paneNav.getHBox());
        borderPane.setLeft(paneContacts.getBorderPane());
        borderPane.setCenter(paneChat.getBorderPane());
    }

    public void selectContact(Contact contact) {
        paneChat.setContact(contact);
    }

    public GUI getGUI() {
        return gui;
    }

    public PaneContacts getPaneContacts() {
        return paneContacts;
    }

    public PaneChat getPaneChat() {
        return paneChat;
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }

    public Contact getMe() {
        return me;
    }

}
