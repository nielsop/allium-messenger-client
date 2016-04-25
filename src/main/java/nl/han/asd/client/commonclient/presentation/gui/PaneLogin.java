package nl.han.asd.client.commonclient.presentation.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Created by Marius on 19-04-16.
 */
public class PaneLogin extends Pane {
    GridPane grid = null;

    public PaneLogin(GUI gui) {
        //Set gridPane
        grid = getGridPane(Pos.CENTER);
        setTitle(grid, "Welcome to the onion messenger", 0, 0, 2, 1);

        //Set labels
        setLabel(grid, "Username:", 0, 1);
        setLabel(grid, "Password:", 0, 2);

        //Set input fields
        final TextField userTextField = getTextField(grid, 1, 1);
        final PasswordField pwBox = getPasswordField(grid, 1, 2);

        //Set buttons
        Button regBtn = getButton(grid, "Register", Pos.BOTTOM_LEFT, 0, 4);
        Button logBtn = getButton(grid, "Login", Pos.BOTTOM_RIGHT, 1, 4);

        //Set text
        final Text actionTarget = getText(grid, TextAlignment.RIGHT, 1, 5);

        logBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (userTextField.getText().length() < 3) actionTarget.setText("Username is too short!");
                else if (pwBox.getText().length() < 3) actionTarget.setText("Password is too short!");
                else actionTarget.setText("Logged in!");
            }
        });

        regBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                gui.setStage(GUI.Page.REGISTER);
            }
        });
    }

    public GridPane getGrid() {
        return grid;
    }
}
