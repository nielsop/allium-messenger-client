package nl.han.asd.project.client.commonclient.presentation.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Created by Marius on 19-04-16.
 */
public class Pane {
    public Text getText(GridPane grid, TextAlignment alignment, int x, int y) {
        final Text t = new Text();
        t.setTextAlignment(alignment); //werkt niet??
        grid.add(t, x, y);
        return t;
    }

    public void setTitle(GridPane grid, String title, int x, int y, int colSpan, int rowSpan) {
        Text sceneTitle = new Text(title);
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, x, y, colSpan, rowSpan);
    }

    public Button getButton(GridPane grid, String label, Pos alignment, int x, int y) {
        Button btn = new Button(label);
        HBox hbBtn = new HBox(0);
        hbBtn.setAlignment(alignment);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, x, y);
        return btn;
    }

    public PasswordField getPasswordField(GridPane grid, int x, int y) {
        final PasswordField pw = new PasswordField();
        grid.add(pw, x, y);
        return pw;
    }

    public void setLabel(GridPane grid, String label, int x, int y) {
        Label l = new Label(label);
        grid.add(l, x, y);
    }

    public TextField getTextField(GridPane grid, int x, int y) {
        final TextField tf = new TextField();
        grid.add(tf, x, y);
        return tf;
    }

    public GridPane getGridPane(Pos alignment) {
        GridPane grid = new GridPane();
        grid.setAlignment(alignment);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        return grid;
    }
}
