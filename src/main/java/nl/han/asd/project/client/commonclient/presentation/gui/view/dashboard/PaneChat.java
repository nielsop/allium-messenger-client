package nl.han.asd.project.client.commonclient.presentation.gui.view.dashboard;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.presentation.gui.view.Pane;
import nl.han.asd.project.client.commonclient.presentation.gui.view.PaneDashboard;

/**
 * Created by Marius on 25-04-16.
 */
public class PaneChat {
    private final GUI gui;
    private final PaneDashboard paneDashboard;

    private BorderPane borderPane = null;

    private PaneChatUserInput paneChatUserInput;
    private PaneChatHistory paneChatHistory;

    public PaneChat(GUI gui, PaneDashboard paneDashboard) {
        this.gui = gui;
        this.paneDashboard = paneDashboard;
        setupPane();
    }

    private void setupPane() {
        paneChatUserInput = new PaneChatUserInput(gui, paneDashboard, this);
        paneChatHistory = new PaneChatHistory(gui, paneDashboard, this);
        borderPane = Pane.getBorderPane(new int[]{0, 0, 0, 0});

        Label contactNameChatTitle = new Label("Contact name");
        contactNameChatTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        borderPane.setTop(contactNameChatTitle);
        borderPane.setCenter(paneChatHistory.getScrollPane());
        borderPane.setBottom(paneChatUserInput.getHBox());
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }

    public PaneChatUserInput getPaneChatUserInput() {
        return paneChatUserInput;
    }

    public PaneChatHistory getPaneChatHistory() {
        return paneChatHistory;
    }
}
