package nl.han.asd.project.client.commonclient.presentation.gui.view.dashboard;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.presentation.gui.view.Pane;
import nl.han.asd.project.client.commonclient.presentation.gui.view.PaneDashboard;
import nl.han.asd.project.client.commonclient.store.Contact;

import static nl.han.asd.project.client.commonclient.presentation.gui.view.Pane.*;

public class PaneChat {
    public static final String FX_BACKGROUND_COLOR_EEE = "-fx-background-color: #EEE;";
    private PaneDashboard paneDashboard;
    private BorderPane borderPane;
    private HBox current;
    private Contact receiver;
    private Contact me;
    private HBox title;
    private VBox chatList;
    private ScrollPane chatPane;

    public PaneChat(PaneDashboard paneDashboard) {
        this.paneDashboard = paneDashboard;
        me = paneDashboard.getMe();

        borderPane = Pane.getBorderPane(new int[] { 0, 0, 0, 0 });
        borderPane.setStyle("-fx-background-color: #EEE; -fx-background: #EEE;");
        borderPane.setTop(getTop());
        borderPane.setCenter(getCenter());
        borderPane.setBottom(getBottom());
    }

    public HBox getTop() {
        title = getHBox(0, new int[] { 5, 5, 5, 5 }, "");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        title.getChildren().add(new Label("Klik op een contact om te chatten."));
        return title;
    }

    public ScrollPane getCenter() {
        String style = "-fx-background-color:transparent; -fx-background: #EEE;";
        chatPane = getScrollPane(true, true, null, null, style);
        return chatPane;
    }

    private void setHBoxMouseEvents(HBox hBox) {
        hBox.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (current != null)
                current.setStyle(FX_BACKGROUND_COLOR_EEE);
            hBox.setStyle("-fx-background-color: #DDD;");
            current = hBox;
        });
        hBox.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            hBox.setStyle(FX_BACKGROUND_COLOR_EEE);
        });
        hBox.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            if (hBox != current)
                hBox.setStyle(FX_BACKGROUND_COLOR_EEE);
        });
    }

    public BorderPane getBottom() {
        BorderPane bPane = Pane.getBorderPane(new int[] { 0, 0, 0, 0 });
        Button send = new Button("send");
        send.setPrefHeight(50);
        TextArea newMessage = new TextArea();
        newMessage.setPrefHeight(50);
        send.setOnMouseClicked(e -> {
            if (newMessage.getText().length() > 0) {
                paneDashboard.getPaneChat()
                        .addMessageToChat(new Message(newMessage.getText(), me, receiver), chatList, true);
                chatPane.setVvalue(1.0);
                newMessage.setText("");
            }
        });
        bPane.setCenter(newMessage);
        bPane.setRight(send);
        return bPane;
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }

    public void setContact(Contact contact) {
        receiver = contact;
        title.getChildren().set(0, new Label(contact.getUsername()));
        chatList = getChatPane(contact);
        chatPane.setContent(chatList);
    }

    public VBox getChatPane(Contact contact) {
        VBox vBox = getVBox(0, new int[] { 0, 0, 0, 0 }, "");
        return vBox;
    }

    private void addMessageToChat(Message message, VBox chat, boolean newMessage) {
        HBox messageBox = getHBox(0, new int[] { 5, 5, 5, 5 }, "-fx-background-color: #EEE;");
        messageBox.getChildren().add(new Text(message.getText()));

        if (message.getSender().getUsername().equals(me.getUsername()))
            messageBox.setAlignment(Pos.TOP_RIGHT);
        else
            messageBox.setAlignment(Pos.TOP_LEFT);

        setHBoxMouseEvents(messageBox);
        chat.getChildren().add(messageBox);

    }
}
