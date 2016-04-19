package nl.han.asd.client.commonclient.presentation;/**
 * Created by Marius on 19-04-16.
 */

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Ui Berichter");
        
        GridPane grid = getGridPane(Pos.CENTER);
        setTitle(grid, "Welkom bij de Ui Berichter", 0, 0, 2, 1);

        setLabel(grid, "Gebruikersnaam:", 0, 1);
        final TextField userTextField = getTextField(grid, 1, 1);

        setLabel(grid, "Wachtwoord:", 0, 2);
        final PasswordField pwBox = getPasswordField(grid, 1, 2);

        Button btn = getButton(grid, "Registreer", Pos.BOTTOM_RIGHT, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (userTextField.getText().length() < 3) actiontarget.setText("Gebruikersnaam is niet ingevuld!");
                else if (pwBox.getText().length() < 3) actiontarget.setText("Wachtwoord is niet ingevuld!");
                else actiontarget.setText("Geregistreerd!");
            }
        });

        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setTitle(GridPane grid, String title, int x, int y, int colSpan, int rowSpan) {
        Text sceneTitle = new Text(title);
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, x, y, colSpan, rowSpan);
    }

    private Button getButton(GridPane grid, String label, Pos alignment, int x, int y) {
        Button btn = new Button(label);
        HBox hbBtn = new HBox(0);
        hbBtn.setAlignment(alignment);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, x, y);
        return btn;
    }

    private PasswordField getPasswordField(GridPane grid, int x, int y) {
        final PasswordField pw = new PasswordField();
        grid.add(pw, x, y);
        return pw;
    }

    private void setLabel(GridPane grid, String label, int x, int y) {
        Label l = new Label(label);
        grid.add(l, x, y);
    }

    private TextField getTextField(GridPane grid, int x, int y) {
        final TextField tf = new TextField();
        grid.add(tf, x, y);
        return tf;
    }

    private GridPane getGridPane(Pos alignment) {
        GridPane grid = new GridPane();
        grid.setAlignment(alignment);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        return grid;
    }
}
