package nl.han.asd.client.commonclient.presentation.gui.view.auth;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import nl.han.asd.client.commonclient.presentation.gui.GUI;
import nl.han.asd.client.commonclient.presentation.gui.view.Pane;

/**
 * Created by Marius on 19-04-16.
 */
public class PaneLogin extends Pane {
    GridPane gridPane = null;

    public PaneLogin(GUI gui) {
        //Set gridPane
        gridPane = getGridPane(Pos.CENTER, new int[]{25, 25, 25, 25});
        setTitle(gridPane, "Welcome to the onion messenger", 0, 0, 2, 1);

        //Set labels
        setLabel(gridPane, "Username:", 0, 1);
        setLabel(gridPane, "Password:", 0, 2);

        //Set input fields
        final TextField userTextField = getTextField(gridPane, 1, 1);
        final PasswordField pwBox = getPasswordField(gridPane, 1, 2);

        //Set buttons
        Button regBtn = getButton(gridPane, "Register", Pos.BOTTOM_LEFT, 0, 4);
        Button logBtn = getButton(gridPane, "Login", Pos.BOTTOM_RIGHT, 1, 4);
        Button dshBtn = getButton(gridPane, "Dashboard", Pos.BOTTOM_LEFT, 0, 6);

        //Set text
        final Text actionTarget = getText(gridPane, TextAlignment.RIGHT, 1, 5);

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

        dshBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                gui.setStage(GUI.Page.DASHBOARD);
            }
        }); 
    }

    public GridPane getGridPane() {
        return gridPane;
    }
}
