package nl.han.asd.project.client.commonclient.presentation.gui.controller.dashboard;

import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.presentation.gui.controller.DashboardController;
import nl.han.asd.project.client.commonclient.presentation.gui.model.dashboard.ChatModel;
import nl.han.asd.project.client.commonclient.presentation.gui.view.dashboard.ChatView;
import nl.han.asd.project.client.commonclient.store.Contact;

import static nl.han.asd.project.client.commonclient.presentation.gui.PaneFactory.getHBox;
import static nl.han.asd.project.client.commonclient.presentation.gui.PaneFactory.getVBox;

/**
 * Created by Marius on 25-04-16.
 */
public class ChatController {
    private ChatModel model;
    private ChatView view;
    private boolean scrollFix = false;

    public ChatController(DashboardController dashboardController) {
        model = new ChatModel(dashboardController);
        view = new ChatView();
        onActions();
    }

    private void onActions() {
        view.getChat().vvalueProperty().addListener((observable, oldValue, newValue) -> {
            if (scrollFix) {
                view.scrollChat(view.getChatMaxHeight());
                scrollFix = false;
            }
        });

        view.getInput().getRight().setOnMouseClicked(e -> {
            if (view.getNewMessage().length() > 0) {
                addMessageToChat(new Message(view.getNewMessage(), model.getCurrentUser(), model.getReceiver()), view.getChatContent(), true);
                view.getChat().setVvalue(1.0);
                view.setNewMessage("");
            }
        });
    }

    private void setChat() {
        view.setChatContent(getChatPane(model.getCurrentUser()), model.getCurrentUser().getUsername());
        model.setReceiver(model.getCurrentUser());
    }

    private VBox getChatPane(Contact contact) {
        VBox vBox = getVBox(0, new int[] { 0, 0, 0, 0 }, "");
        //        for (Message message : paneDashboard.getMessages(contactStore)) {
        //            addMessageToChat(message, vBox, false);
        //        }
        return vBox;
    }

    public void addMessageToChat(Message message, VBox chat, boolean newMessage) {
        HBox messageBox = getHBox(0, new int[] { 5, 5, 5, 5 }, "-fx-background-color: #EEE;");
        messageBox.getChildren().add(new Text(message.getText()));


        if (message.getSender().getUsername().equals(model.getCurrentUser().getUsername())) {
            messageBox.setAlignment(Pos.TOP_RIGHT);
        } else {
            messageBox.setAlignment(Pos.TOP_LEFT);
        }

        setHBoxMouseEvents(messageBox);
        chat.getChildren().add(messageBox);
        if (newMessage) {
            model.sendMessage(message);
            scrollFix = true;
        }
    }

    private void setHBoxMouseEvents(HBox hBox) {
        hBox.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            view.setSelectedMessage(hBox);

        });
        hBox.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            view.setEnteredMessage(hBox);

        });
        hBox.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            view.setExitedMessage(hBox);
        });
    }

    public BorderPane getBorderPane() {
        return view.getBorderPane();
    }
}
