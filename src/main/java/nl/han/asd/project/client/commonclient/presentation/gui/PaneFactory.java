package nl.han.asd.project.client.commonclient.presentation.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

/**
 * Created by Marius on 19-04-16.
 */
public class PaneFactory {

    private PaneFactory(){
        
    }

    public static GridPane getGridPane(Pos alignment, int[] padding) {
        GridPane pane = new GridPane();
        pane.setAlignment(alignment);
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(padding[0], padding[1], padding[2], padding[3]));
        return pane;
    }

    public static BorderPane getBorderPane(int[] padding) {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(padding[0], padding[1], padding[2], padding[3]));
        return pane;
    }

    public static ScrollPane getScrollPane(boolean fitWidth, boolean fitHeight, int[] width, int[] height,
                                           String style) {
        ScrollPane pane = new ScrollPane();
        pane.setFitToWidth(fitWidth);
        pane.setFitToHeight(fitHeight);
        pane.setStyle(style);
        if (width != null) {
            if (width[0] != 0)
                pane.setMinWidth(width[0]);
            if (width[1] != 0 && width[1] > width[0])
                pane.setMaxWidth(width[1]);
        }
        if (height != null) {
            if (height[0] != 0)
                pane.setMinHeight(height[0]);
            if (height[1] != 0 && height[1] > height[0])
                pane.setMaxHeight(height[1]);
        }
        return pane;
    }

    public static HBox getHBox(int margin, int[] padding, String style) {
        HBox hbox = new HBox();
        hbox.setStyle(style);
        hbox.setPadding(new Insets(padding[0], padding[1], padding[2], padding[3]));
        hbox.setSpacing(margin);
        return hbox;
    }

    public static VBox getVBox(int margin, int[] padding, String style) {
        VBox vbox = new VBox();
        vbox.setStyle(style);
        vbox.setPadding(new Insets(padding[0], padding[1], padding[2], padding[3]));
        vbox.setSpacing(margin);
        return vbox;
    }

    public static void fancyLabel(Label label, GUI gui) {
        label.setTextFill(Paint.valueOf("#888"));
        label.setOnMouseEntered(e -> {
            label.setTextFill(Paint.valueOf("#000"));
            gui.getScene().setCursor(Cursor.HAND);
        });
        label.setOnMouseExited(e -> {
            label.setTextFill(Paint.valueOf("#888"));
            gui.getScene().setCursor(Cursor.DEFAULT);
        });
    }
}
