package nl.han.asd.project.client.commonclient.presentation.gui.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

    public GridPane getGridPane(Pos alignment, int[] padding) {
        GridPane pane = new GridPane();
        pane.setAlignment(alignment);
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(padding[0], padding[1], padding[2], padding[3]));
        return pane;
    }

    public BorderPane getBorderPane(int[] padding) {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(padding[0], padding[1], padding[2], padding[3]));
        return pane;
    }

    public ScrollPane getScrollPane(boolean fitWidth, boolean fitHeight, int[] width, int[] height, String style) {
        ScrollPane pane = new ScrollPane();
        pane.setFitToWidth(fitWidth);
        pane.setFitToHeight(fitHeight);
        pane.setStyle(style);
        if (width != null) {
            if (width[0] > 0) pane.setMinWidth(width[0]);
            if (width[1] > 0) pane.setMaxWidth(width[1]);
        }
        if (height != null) {
            if (height[0] != 0) pane.setMinHeight(height[0]);
            if (height[1] != 0) pane.setMaxHeight(height[1]);
        }
        return pane;
    }

    public HBox getHBox(int margin, int[] padding, String style) {
        HBox hbox = new HBox();
        hbox.setStyle(style);
        hbox.setPadding(new Insets(padding[0], padding[1], padding[2], padding[3]));
        hbox.setSpacing(margin);
        return hbox;
    }

    public VBox getVBox(int margin, int[] padding, String style) {
        VBox vbox = new VBox();
        vbox.setStyle(style);
        vbox.setPadding(new Insets(padding[0], padding[1], padding[2], padding[3]));
        vbox.setSpacing(margin);
        return vbox;
    }
}
