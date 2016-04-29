package nl.han.asd.project.client.commonclient.presentation.gui.view.dashboard;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.presentation.gui.view.Pane;
import nl.han.asd.project.client.commonclient.presentation.gui.view.PaneDashboard;
import nl.han.asd.project.client.commonclient.store.Contact;

import java.util.ArrayList;

import static nl.han.asd.project.client.commonclient.presentation.gui.view.Pane.*;

/**
 * Created by Marius on 25-04-16.
 */
public class PaneChat {
    private PaneDashboard paneDashboard;
    private BorderPane borderPane;
    private HBox current;
    public Contact me = new Contact("Marius", "asdf4321", true);

    public PaneChat(PaneDashboard paneDashboard) {
        this.paneDashboard = paneDashboard;

        borderPane = Pane.getBorderPane(new int[]{0, 0, 0, 0});
        borderPane.setTop(getTop());
        borderPane.setCenter(getCenter());
        borderPane.setBottom(getBottom());
    }

    public HBox getTop() {
        HBox hBox = getHBox(0, new int[]{5, 5, 5, 5}, "");
        Label title = new Label("");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        hBox.getChildren().add(title);
        return hBox;
    }

    public ScrollPane getCenter() {
        String style = "-fx-background-color:transparent; -fx-background: #EEE;";
        ScrollPane scrollPane = getScrollPane(true, true, null, null, style);

        VBox chatList = getVBox(0, new int[]{0, 0, 0, 0}, "");

        for (Message message : createTestMessages()) {
            HBox messageBox = getHBox(0, new int[]{5, 5, 5, 5}, "-fx-background-color: #EEE;");
            messageBox.getChildren().add(new Text(message.getText()));
            if (message.getSender().getUsername().equals(me.getUsername())) messageBox.setAlignment(Pos.TOP_RIGHT);
            else messageBox.setAlignment(Pos.TOP_LEFT);
            setHBoxMouseEvents(messageBox);
            chatList.getChildren().add(messageBox);
        }

        scrollPane.setContent(chatList);
        return scrollPane;
    }

    private ArrayList<Message> createTestMessages() {
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(new Message("bericht 1", new Contact("Dennis", "asdf4321", false), me));
        messages.add(new Message("bericht 2", new Contact("Dennis", "asdf4321", false), me));
        messages.add(new Message("bericht 3", me, new Contact("Dennis", "asdf4321", false)));
        messages.add(new Message("bericht 4", new Contact("Dennis", "asdf4321", false), me));
        messages.add(new Message("bericht 5", me, new Contact("Dennis", "asdf4321", false)));
        messages.add(new Message("bericht 6", me, new Contact("Dennis", "asdf4321", false)));
        messages.add(new Message("bericht 7", new Contact("Dennis", "asdf4321", false), me));
        messages.add(new Message("bericht 8", new Contact("Dennis", "asdf4321", false), me));
        messages.add(new Message("bericht 9", me, new Contact("Dennis", "asdf4321", false)));
        messages.add(new Message("bericht 10", me, new Contact("Dennis", "asdf4321", false)));
        messages.add(new Message("bericht 11", new Contact("Dennis", "asdf4321", false), me));
        messages.add(new Message("bericht 12", me, new Contact("Dennis", "asdf4321", false)));
        messages.add(new Message("bericht 13", new Contact("Dennis", "asdf4321", false), me));
        messages.add(new Message("bericht 14", me, new Contact("Dennis", "asdf4321", false)));
        messages.add(new Message("bericht 15", new Contact("Dennis", "asdf4321", false), me));
        messages.add(new Message("bericht 16", new Contact("Dennis", "asdf4321", false), me));
        messages.add(new Message("bericht 17", me, new Contact("Dennis", "asdf4321", false)));
        messages.add(new Message("bericht 18", me, new Contact("Dennis", "asdf4321", false)));
        messages.add(new Message("bericht 19", new Contact("Dennis", "asdf4321", false), me));
        messages.add(new Message("bericht 20", me, new Contact("Dennis", "asdf4321", false)));
        messages.add(new Message("bericht 21", new Contact("Dennis", "asdf4321", false), me));
        messages.add(new Message("bericht 22", me, new Contact("Dennis", "asdf4321", false)));
        messages.add(new Message("bericht 23", me, new Contact("Dennis", "asdf4321", false)));
        messages.add(new Message("bericht 24", new Contact("Dennis", "asdf4321", false), me));
        messages.add(new Message("bericht 25", new Contact("Dennis", "asdf4321", false), me));
        messages.add(new Message("bericht 26", me, new Contact("Dennis", "asdf4321", false)));
        messages.add(new Message("bericht 27", me, new Contact("Dennis", "asdf4321", false)));
        messages.add(new Message("bericht 28", new Contact("Dennis", "asdf4321", false), me));
        messages.add(new Message("bericht 29", me, new Contact("Dennis", "asdf4321", false)));
        return messages;
    }

    private void setHBoxMouseEvents(HBox hBox) {
        hBox.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (current != null) current.setStyle("-fx-background-color: #EEE;");
            hBox.setStyle("-fx-background-color: #DDD;");
            current = hBox;
        });
        hBox.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            hBox.setStyle("-fx-background-color: #DDD;");
        });
        hBox.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            if (hBox != current) hBox.setStyle("-fx-background-color: #EEE;");
        });
    }

    public GridPane getBottom() {
        GridPane gridPane = getGridPane(Pos.TOP_LEFT, new int[]{0, 0, 0, 0});
        Button send = new Button("verzend");
        TextArea newMessage = new TextArea();
        newMessage.setPrefSize( Double.MAX_VALUE, Double.MAX_VALUE );
        newMessage.setPrefHeight(100);
        gridPane.add(newMessage, 0, 0);
        gridPane.add(send, 1, 0);
        return gridPane;
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }

    public void setContact(Label contact) {
        //borderPane.getTop().setText(contact.getText());
    }
}
