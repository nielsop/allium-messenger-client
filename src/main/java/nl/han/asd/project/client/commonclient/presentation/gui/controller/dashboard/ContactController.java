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
    private ContactModel model;
    private ContactView view;
    private HBox current;

    public ContactController(DashboardController dashboardController) {
        model = new ContactModel(dashboardController);
        view = new ContactView();
        fillContactList();
        onActions();
    }

    private void onActions() {
    }

    private void fillContactList() {
        if (model.getContacts() != null) {
            model.getContacts().stream().filter(contact -> contact != null).forEach(contact -> {
                Label name = new Label(contact.getUsername());
                HBox contactBox = getHBox(0, new int[] { 5, 5, 5, 5 }, "-fx-background-color: #FFF;");
                setHBoxMouseEvents(contactBox, contact);
                contactBox.getChildren().add(name);
                view.getContactList().getChildren().add(contactBox);
            });
        }
    }

    private void setHBoxMouseEvents(HBox hBox, Contact contact) {
        hBox.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            model.selectContact(contact);
            if (current != null)
                current.setStyle("-fx-background-color: #FFF;");
            hBox.setStyle("-fx-background-color: #EEE;");
            current = hBox;
            model.setCurrectContact(contact);
        });
        hBox.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            hBox.setStyle("-fx-background-color: #EEE;");
        });
        hBox.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            if (hBox != current)
                hBox.setStyle("-fx-background-color: #FFF;");
        });
    }

    public BorderPane getBorderPane() {
        return view.getBorderPane();
    }
}
