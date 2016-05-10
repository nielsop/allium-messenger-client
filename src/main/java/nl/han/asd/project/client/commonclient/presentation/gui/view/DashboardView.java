package nl.han.asd.project.client.commonclient.presentation.gui.view;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * Created by Marius on 25-04-16.
 */
public class DashboardView {
    private BorderPane borderPane;

    public DashboardView() {
        borderPane = PaneFactory.getBorderPane(new int[]{0, 0, 0, 0});
        borderPane.setStyle("-fx-background-color: #FFF; -fx-background: #FFF;");
    }

    public void setBorderPaneContent(HBox top, BorderPane left, BorderPane center) {
        borderPane.setTop(top);
        borderPane.setLeft(left);
        borderPane.setCenter(center);
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }
}
