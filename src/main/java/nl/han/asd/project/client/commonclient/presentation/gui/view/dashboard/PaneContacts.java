package nl.han.asd.project.client.commonclient.presentation.gui.view.dashboard;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nl.han.asd.project.client.commonclient.presentation.gui.view.Pane;
import nl.han.asd.project.client.commonclient.presentation.gui.view.PaneDashboard;
import nl.han.asd.project.client.commonclient.store.Contact;

import java.util.ArrayList;

import static nl.han.asd.project.client.commonclient.presentation.gui.view.Pane.*;

/**
 * Created by Marius on 25-04-16.
 */
public class PaneContacts {
    private PaneDashboard paneDashboard;
    private BorderPane borderPane;
    private HBox current;

    public PaneContacts(PaneDashboard paneDashboard) {
        this.paneDashboard = paneDashboard;

        borderPane = Pane.getBorderPane(new int[]{0,0,0,0});
        borderPane.setTop(getTop());
        borderPane.setCenter(getCenter());
        borderPane.setBottom(getBottom());
    }

    public HBox getTop() {
        HBox hBox = getHBox(0, new int[]{5,5,5,5}, "");
        Label title = new Label("Contacten");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        hBox.getChildren().add(title);
        return hBox;
    }

    public ScrollPane getCenter() {
        String style = "-fx-background-color:transparent; -fx-background: #DDD;";
        ScrollPane scrollPane = getScrollPane(true, true, null, null, style);

        VBox contactList = getVBox(0, new int[]{0,0,0,0}, "");

        for (Contact contact : createTestContacts()) {
            Label name = new Label(contact.getUsername());
            HBox contactBox = getHBox(0, new int[]{5,5,5,5}, "-fx-background-color: #FFF;");
            setHBoxMouseEvents(contactBox);
            contactBox.getChildren().add(name);
            contactList.getChildren().add(contactBox);
        }

        scrollPane.setContent(contactList);
        return scrollPane;
    }

    private void setHBoxMouseEvents(HBox hBox) {
        hBox.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            paneDashboard.selectContact((Label) hBox.getChildren().get(0));
            if (current != null) current.setStyle("-fx-background-color: #FFF;");
            hBox.setStyle("-fx-background-color: #EEE;");
            current = hBox;
        });
        hBox.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            hBox.setStyle("-fx-background-color: #EEE;");
        });
        hBox.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            if (hBox != current) hBox.setStyle("-fx-background-color: #FFF;");
        });
    }

    public HBox getBottom() {
        HBox hBox = getHBox(5, new int[]{0,0,0,0}, "");
        Button edit = new Button("Edit");
        Button add = new Button("Add");
        Button remove = new Button("Remove");
        hBox.getChildren().addAll(add, edit, remove);
        return hBox;
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }





    private ArrayList<Contact> createTestContacts() {
        ArrayList<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("Bram", "asdf4321", true));
        contacts.add(new Contact("Marius", "asdf4321", true));
        contacts.add(new Contact("Niels", "asdf4321", false));
        contacts.add(new Contact("Jevgeni", "asdf4321", true));
        contacts.add(new Contact("Dennis", "asdf4321", true));
        contacts.add(new Contact("Kenny", "asdf4321", false));
        contacts.add(new Contact("Julius", "asdf4321", false));
        contacts.add(new Contact("Bram", "asdf4321", true));
        contacts.add(new Contact("Marius", "asdf4321", true));
        contacts.add(new Contact("Niels", "asdf4321", false));
        contacts.add(new Contact("Jevgeni", "asdf4321", true));
        contacts.add(new Contact("Dennis", "asdf4321", true));
        contacts.add(new Contact("Kenny", "asdf4321", false));
        contacts.add(new Contact("Julius", "asdf4321", false));
        contacts.add(new Contact("Bram", "asdf4321", true));
        contacts.add(new Contact("Marius", "asdf4321", true));
        contacts.add(new Contact("Niels", "asdf4321", false));
        contacts.add(new Contact("Jevgeni", "asdf4321", true));
        contacts.add(new Contact("Dennis", "asdf4321", true));
        contacts.add(new Contact("Kenny", "asdf4321", false));
        contacts.add(new Contact("Julius", "asdf4321", false));
        contacts.add(new Contact("Bram", "asdf4321", true));
        contacts.add(new Contact("Marius", "asdf4321", true));
        contacts.add(new Contact("Niels", "asdf4321", false));
        contacts.add(new Contact("Jevgeni", "asdf4321", true));
        contacts.add(new Contact("Dennis", "asdf4321", true));
        contacts.add(new Contact("Kenny", "asdf4321", false));
        contacts.add(new Contact("Julius", "asdf4321", false));
        return contacts;
    }
}
