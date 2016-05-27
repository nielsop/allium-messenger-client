package nl.han.asd.project.client.commonclient.presentation.gui.view.dashboard;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nl.han.asd.project.client.commonclient.presentation.gui.PaneFactory;

import static nl.han.asd.project.client.commonclient.presentation.gui.PaneFactory.*;

/**
 * Created by Marius on 25-04-16.
 */
public class ContactView {
    private BorderPane borderPane;
    private HBox top = getTop();
    private VBox contactList;
    private ScrollPane center = getCenter();
    private Button edit;
    private Button remove;
    private Button add;
    private HBox bottom = getBottom();

    public ContactView() {
        borderPane = PaneFactory.getBorderPane(new int[] { 0, 0, 0, 0 });
        borderPane.setStyle("-fx-background-color: #FFF; -fx-background: #FFF;");
        borderPane.setTop(top);
        borderPane.setCenter(center);
        borderPane.setBottom(bottom);
    }

    private HBox getTop() {
        HBox hBox = getHBox(0, new int[] { 5, 5, 5, 5 }, "");
        Label title = new Label("Contacten");
        title.setStyle("-fx-font-size: 15px;");
        hBox.getChildren().add(title);
        return hBox;
    }

    private ScrollPane getCenter() {
        String style = "-fx-background-color:#FFF; -fx-background: #FFF;";
        ScrollPane scrollPane = getScrollPane(true, true, null, null, style);
        contactList = getVBox(0, new int[] { 0, 0, 0, 0 }, "");
        scrollPane.setContent(contactList);
        return scrollPane;
    }

    private HBox getBottom() {
        HBox hBox = getHBox(5, new int[] { 0, 0, 0, 0 }, "");
        edit = new Button("Edit");
        add = new Button("Add");
        remove = new Button("Remove");
        hBox.getChildren().addAll(add, edit, remove);
        return hBox;
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }

    public VBox getContactList() {
        return contactList;
    }
}
