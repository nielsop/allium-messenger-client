package nl.han.asd.project.client.commonclient.presentation.gui.view.dashboard;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nl.han.asd.project.client.commonclient.presentation.gui.view.PaneFactory;

import static nl.han.asd.project.client.commonclient.presentation.gui.view.PaneFactory.getHBox;
import static nl.han.asd.project.client.commonclient.presentation.gui.view.PaneFactory.getScrollPane;

/**
 * Created by Marius on 25-04-16.
 */
public class ChatView {
    private BorderPane borderPane;
    private HBox top = getTop();
    private ScrollPane center = getCenter();
    private BorderPane bottom = getBottom();
    private TextArea newMessage;
    private Button sendNewMessage;

    public ChatView() {
        borderPane = PaneFactory.getBorderPane(new int[]{0, 0, 0, 0});
        borderPane.setStyle("-fx-background-color: #EEE; -fx-background: #EEE;");
        borderPane.setTop(top);
        borderPane.setCenter(center);
        borderPane.setBottom(bottom);
    }

    private HBox getTop() {
        HBox top = getHBox(0, new int[]{5, 5, 5, 5}, "");
        top.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        top.getChildren().add(new Label("Klik op een contact om te chatten."));
        return top;
    }

    private ScrollPane getCenter() {
        String style = "-fx-background-color:transparent; -fx-background: #EEE;";
        ScrollPane center = getScrollPane(true, true, null, null, style);
        return center;
    }

    private BorderPane getBottom() {
        BorderPane bottom = PaneFactory.getBorderPane(new int[]{0, 0, 0, 0});
        sendNewMessage = new Button("send");
        sendNewMessage.setPrefHeight(50);
        newMessage = new TextArea();
        newMessage.setPrefHeight(50);
        bottom.setCenter(newMessage);
        bottom.setRight(sendNewMessage);
        return bottom;
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }

    public void setContact(String contact) {
        top.getChildren().set(0, new Label(contact));
    }

    public HBox getNav() {
        return top;
    }

    public ScrollPane getChat() {
        return center;
    }

    public BorderPane getInput() {
        return bottom;
    }

    public void scrollChat(double position) {
        center.setVvalue(position);
    }

    public double getChatMaxHeight() {
        return center.getVmax();
    }

    public String getNewMessage() {
        return newMessage.getText();
    }

    public void setNewMessage(String newMessage) {
        this.newMessage.setText(newMessage);
    }

    public Button getSendNewMessage() {
        return sendNewMessage;
    }

    public void setChatContent(VBox chatContent, String username) {
        center.setContent(chatContent);
        setContact(username);
    }

    public VBox getChatContent() {
        return (VBox) center.getContent();
    }
}
