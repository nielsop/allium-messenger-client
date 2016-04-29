package nl.han.asd.project.client.commonclient.presentation.gui.view.dashboard;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.presentation.gui.view.PaneDashboard;

import static nl.han.asd.project.client.commonclient.presentation.gui.view.Pane.fancyLabel;

/**
 * Created by DDulos on 28-Apr-16.
 */
public class PaneChatUserInput{
    private final GUI gui;
    private final PaneDashboard paneDashboard;
    private final PaneChat paneChat;

    private Label userInputLabel;
    private TextField userInputText;
    private Label sendMessageButton;
    private Label sendFileButton;

    private HBox hBox;

    public PaneChatUserInput(GUI gui, PaneDashboard paneDashboard, PaneChat paneChat) {

        this.gui = gui;
        this.paneDashboard = paneDashboard;
        this.paneChat = paneChat;

        userInputLabel = new Label("Type hier: ");

        userInputText = new TextField();
        userInputText.setStyle("-fx-min-width: inherit;");

        sendMessageButton = new Label("Verzend");
        fancyLabel(sendMessageButton, gui);
        sendMessageButton.setOnMouseClicked(e -> paneDashboard.getPaneChat().getPaneChatHistory().addToChatBox(userInputText.getText()));

        sendFileButton = new Label("Bestand");
        sendFileButton.setVisible(false);
        String style = "-fx-background-color:transparent";
        hBox = nl.han.asd.project.client.commonclient.presentation.gui.view.Pane.getHBox(10, new int[]{10, 10, 10, 10}, style);
        hBox.getChildren().add(userInputLabel);
        hBox.getChildren().add(userInputText);
        hBox.getChildren().add(sendMessageButton);
        hBox.getChildren().add(sendFileButton);
    }

    public Pane getHBox() {
        return hBox;
    }
}
