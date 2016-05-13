package nl.han.asd.project.client.commonclient.presentation.gui.view.dashboard;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nl.han.asd.project.client.commonclient.presentation.gui.PaneFactory;
import nl.han.asd.project.client.commonclient.presentation.gui.view.Styles;

import static nl.han.asd.project.client.commonclient.presentation.gui.PaneFactory.*;

/**
 * Created by Marius on 25-04-16.
 */
public class ContactView {
    private BorderPane borderPane;
    private HBox top = getTop();
    private ScrollPane center = getCenter();
    private HBox bottom = getBottom();
    private VBox contactList;
    private JFXButton edit;
    private JFXButton remove;
    private JFXButton add;

    public ContactView() {
        borderPane = PaneFactory.getBorderPane(new int[] { 0, 0, 0, 0 });
        borderPane.setStyle("-fx-background-color: " + Styles.WHITE + "; -fx-background: " + Styles.WHITE + ";");
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
        String style = "-fx-background-color: " + Styles.WHITE + "; -fx-background: " + Styles.WHITE + ";";
        ScrollPane scrollPane = getScrollPane(true, true, null, null, style);
        contactList = getVBox(0, new int[] { 0, 0, 0, 0 }, "");
        scrollPane.setContent(contactList);
        return scrollPane;
    }

    private HBox getBottom() {
        HBox hBox = getHBox(5, new int[] { 0, 0, 0, 0 }, "");
        edit = new JFXButton("Edit");
        edit.setStyle(Styles.FX_BUTTON_RAISED);
        add = new JFXButton("Add");
        add.setStyle(Styles.FX_BUTTON_RAISED);
        remove = new JFXButton("Remove");
        remove.setStyle(Styles.FX_BUTTON_RAISED);
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
