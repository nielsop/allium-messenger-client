package nl.han.asd.project.client.commonclient.presentation.gui.controller.dashboard;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import nl.han.asd.project.client.commonclient.presentation.gui.controller.DashboardController;
import nl.han.asd.project.client.commonclient.presentation.gui.model.dashboard.ContactModel;
import nl.han.asd.project.client.commonclient.presentation.gui.view.dashboard.ContactView;
import nl.han.asd.project.client.commonclient.store.Contact;

import static nl.han.asd.project.client.commonclient.presentation.gui.PaneFactory.getHBox;

/**
 * Created by Marius on 25-04-16.
 */
public class ContactController {
    private static final String STYLE_BACKGROUND_WHITE = "-fx-background-color: #FFF;";
    private static final String STYLE_BACKGROUND_LIGHTGREY = "-fx-background-color: #EEE;";
    private ContactModel model;
    private ContactView view;
    private HBox current;

    public ContactController(DashboardController dashboardController) {
        model = new ContactModel(dashboardController);
        view = new ContactView();
        fillContactList();
    }

    private void fillContactList() {
        if (model.getContacts() != null)
            model.getContacts().stream().filter(contact -> contact != null).forEach(contact -> {
                Label name = new Label(contact.getUsername());
                HBox contactBox = getHBox(0, new int[]{5, 5, 5, 5}, STYLE_BACKGROUND_WHITE);
                setHBoxMouseEvents(contactBox, contact);
                contactBox.getChildren().add(name);
                view.getContactList().getChildren().add(contactBox);
            });
    }

    private void setHBoxMouseEvents(HBox hBox, Contact contact) {
        hBox.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            model.selectContact(contact);
            if (current != null) current.setStyle(STYLE_BACKGROUND_WHITE);
            hBox.setStyle(STYLE_BACKGROUND_LIGHTGREY);
            current = hBox;
            model.setSelectedContact(contact);
        });
        hBox.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> hBox.setStyle(STYLE_BACKGROUND_LIGHTGREY));
        hBox.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            if (hBox != current) {
                hBox.setStyle(STYLE_BACKGROUND_WHITE);
            }
        });
    }

    public BorderPane getBorderPane() {
        return view.getBorderPane();
    }
}
