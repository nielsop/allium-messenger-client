package nl.han.asd.project.client.commonclient.presentation.gui.view.dashboard;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nl.han.asd.project.client.commonclient.presentation.gui.PaneFactory;
import nl.han.asd.project.client.commonclient.presentation.gui.view.Styles;

import static nl.han.asd.project.client.commonclient.presentation.gui.PaneFactory.getHBox;
import static nl.han.asd.project.client.commonclient.presentation.gui.PaneFactory.getScrollPane;

/**
 * Created by Marius on 25-04-16.
 */
public class ChatView {
    private BorderPane borderPane;
    private HBox top = getTop();
    private ScrollPane center = getCenter();
    private BorderPane bottom = getBottom();
    private JFXTextArea messageTextArea;
    private JFXButton btnSendMessage;
    private HBox current;

    public ChatView() {
        borderPane = PaneFactory.getBorderPane(new int[] { 0, 0, 0, 0 });
        borderPane.setStyle("-fx-background-color: " + Styles.LIGHTGREY2 + "; -fx-background: " + Styles.LIGHTGREY2 + ";");
        borderPane.setTop(top);
        borderPane.setCenter(center);
        borderPane.setBottom(bottom);
    }

    private HBox getTop() {
        HBox temp = getHBox(0, new int[]{5, 5, 5, 5}, "");
        temp.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        temp.getChildren().add(new Label("Klik op een contact om te chatten."));
        return temp;
    }

    private ScrollPane getCenter() {
        String style = "-fx-background-color:transparent; -fx-background: #EEE;";
        return getScrollPane(true, true, null, null, style);
    }

    private BorderPane getBottom() {
        BorderPane temp = PaneFactory.getBorderPane(new int[]{0, 0, 0, 0});
        btnSendMessage = new JFXButton("Send");
        btnSendMessage.setStyle(Styles.FX_BUTTON_RAISED);
        btnSendMessage.setPrefHeight(50);
        messageTextArea = new JFXTextArea();
        messageTextArea.setPrefHeight(50);
        temp.setCenter(messageTextArea);
        temp.setRight(btnSendMessage);
        return temp;
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

    public String getMessageTextArea() {
        return messageTextArea.getText();
    }

    public void setMessageTextArea(String messageTextArea) {
        this.messageTextArea.setText(messageTextArea);
    }

    public JFXButton getBtnSendMessage() {
        return btnSendMessage;
    }

    public void setChatContent(VBox chatContent, String username) {
        center.setContent(chatContent);
        setContact(username);
    }

    public VBox getChatContent() {
        return (VBox) center.getContent();
    }

    public void setSelectedMessage(HBox hBox) {
        if (current != null) {
            current.setStyle("-fx-background-color: " + Styles.LIGHTGREY2 + ";");
        }
        hBox.setStyle("-fx-background-color: " + Styles.LIGHTGREY + ";");
        current = hBox;
    }

    public void setEnteredMessage(HBox hBox) {
        hBox.setStyle("-fx-background-color: " + Styles.LIGHTGREY + ";");
    }

    public void setExitedMessage(HBox hBox) {
        if (hBox != current) {
            hBox.setStyle("-fx-background-color: " + Styles.LIGHTGREY2 + ";");
        }
    }
}
