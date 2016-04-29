package nl.han.asd.project.client.commonclient.presentation.gui.view.dashboard;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import nl.han.asd.project.client.commonclient.presentation.gui.view.Pane;
import nl.han.asd.project.client.commonclient.presentation.gui.view.PaneDashboard;

/**
 * Created by DDulos on 28-Apr-16.
 */
public class PaneChatHistory extends Pane {
    private PaneDashboard paneDashboard;
    private Label label;
    private ScrollPane scrollPane;
    private VBox chatBox;

    public PaneChatHistory(PaneDashboard paneDashboard) {
        this.paneDashboard = paneDashboard;

        String style = "-fx-background-color:transparent;";
        scrollPane = getScrollPane(false, false, new int[]{600, 0}, null, style);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setContent(getContent());
    }

    private VBox getContent() {
        String style = "-fx-background-color:transparent;";
        chatBox = getVBox(10, new int[]{10, 10, 10, 10}, style);
        label = new Label("chat entry1");
        chatBox.getChildren().add(label);
        chatBox.getChildren().add(new Label("chat entry2"));
        chatBox.getChildren().add(new Label("chat entry3"));
        chatBox.getChildren().add(new Label("chat entry4"));
        return chatBox;
    }

    public void addToChatBox(String labelText) {
        chatBox.getChildren().add(new Label(labelText));
        scrollPane.setVvalue(scrollPane.getVmax());
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }
}
