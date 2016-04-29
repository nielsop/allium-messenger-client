package nl.han.asd.project.client.commonclient.presentation.gui.view.dashboard;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.presentation.gui.view.Pane;
import nl.han.asd.project.client.commonclient.presentation.gui.view.PaneDashboard;
import nl.han.asd.project.client.commonclient.store.Contact;

import java.util.ArrayList;

/**
 * Created by Marius on 25-04-16.
 */
public class PaneContacts extends Pane {
    private final GUI gui;
    private final PaneDashboard paneDashboard;
    ScrollPane scrollPane;
    private HBox currentSelectedContact;

    // Contact box background styles
    private static final String DEFAULT_HBOX_STYLE = "-fx-background-color: #FFF;";
    private static final String HOVER_HBOX_STYLE = "-fx-background-color: #EEE;";
    private static final String CLICKED_HBOX_STYLE = "-fx-background-color: #CCC;";

    // Online status styles
    private static final String ONLINE_CONTACT_TEXTSTYLE = "-fx-text-fill: #000;";
    private static final String OFFLINE_CONTACT_TEXTSTYLE = "-fx-text-fill: #999;";

    // TODO set to model
    ArrayList<Contact> contacts = new ArrayList<>();
    private VBox contactListBox;


    public PaneContacts(GUI gui, PaneDashboard paneDashboard) {
        this.gui = gui;
        this.paneDashboard = paneDashboard;
        setupPane();
    }

    private void setupPane() {
        String style = "-fx-background-color:transparent; -fx-background: #DDD;";
        scrollPane = getScrollPane(true, false, new int[]{200, 300}, null, style);
        scrollPane.setContent(getContent());
    }

    // TODO set to model
    private void createTestContacts() {
        contacts.add(new Contact("Bram", "asdf4321", true));
        contacts.add(new Contact("Marius", "asdf4321", true));
        contacts.add(new Contact("Niels", "asdf4321", false));
        contacts.add(new Contact("Jevgeni", "asdf4321", true));
        contacts.add(new Contact("Dennis", "asdf4321", true));
        contacts.add(new Contact("Kenny", "asdf4321", false));
        contacts.add(new Contact("Julius", "asdf4321", false));

    }

    private VBox getContent() {
        contactListBox = getVBox(10, new int[]{10, 10, 10, 10}, DEFAULT_HBOX_STYLE);
        setPaneTitle();
        addContactsToList();
        return contactListBox;
    }

    private void setPaneTitle() {
        Label paneTitle = new Label("Contacten");
        paneTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 15px");
        contactListBox.getChildren().add(paneTitle);
    }

    private void addContactsToList() {
        // TODO test, remove
        createTestContacts();

        for (Contact contact : contacts) {
            Label contactLabel = new Label(contact.getUsername());
            if (contact.isOnline())
                contactLabel.setStyle(ONLINE_CONTACT_TEXTSTYLE);
            else
                contactLabel.setStyle(OFFLINE_CONTACT_TEXTSTYLE);

            HBox contactBox = Pane.getHBox(0, new int[]{0, 0, 0, 0}, "-fx-background-color: #FFF;");
            contactBox.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleContactOnClick(contactBox));
            setContactHoverStyle(contactBox);
            contactBox.getChildren().add(contactLabel);
            contactListBox.getChildren().add(contactBox);
        }
    }

    private void setContactHoverStyle(final HBox contactBox) {
        contactBox.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> changeContactBoxStyle(contactBox, HOVER_HBOX_STYLE));
        contactBox.addEventHandler(MouseEvent.MOUSE_EXITED, event -> changeContactBoxStyle(contactBox, DEFAULT_HBOX_STYLE));
    }

    private void changeContactBoxStyle(HBox contactBox, String style) {
        if (contactBox != currentSelectedContact) {
            contactBox.setStyle(style);
        }
    }

    private void handleContactOnClick(HBox selectedContact) {
        if (currentSelectedContact != null)
            currentSelectedContact.setStyle(DEFAULT_HBOX_STYLE);
        currentSelectedContact = selectedContact;
        Label label = (Label) selectedContact.getChildren().get(0);
        currentSelectedContact.setStyle(CLICKED_HBOX_STYLE);
        String test = "Contact selected: " + label.getText();
        System.out.println(test);
//        paneDashboard.getPaneChat().setLabelText(test);

    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }
}
