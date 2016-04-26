package nl.han.asd.project.client.commonclient.presentation.gui.view.auth;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.presentation.gui.view.Pane;
import nl.han.asd.project.protocol.HanRoutingProtocol;

/**
 * Created by Marius on 19-04-16.
 */
public class PaneRegister extends Pane {
    GridPane gridPane = null;

    public PaneRegister(GUI gui) {
        //Set gridPane
        gridPane = getGridPane(Pos.CENTER, new int[]{25, 25, 25, 25});
        setTitle(gridPane, "Welcome to the onion messenger", 0, 0, 2, 1);

        //Set labels
        setLabel(gridPane, "Username:", 0, 1);
        setLabel(gridPane, "Password:", 0, 2);
        setLabel(gridPane, "Password repeat:", 0, 3);

        //Set input fields
        final TextField userTextField = getTextField(gridPane, 1, 1);
        final PasswordField pwBox = getPasswordField(gridPane, 1, 2);
        final PasswordField pwBox2 = getPasswordField(gridPane, 1, 3);

        //Set buttons
        Button backBtn = getButton(gridPane, "Cancel", Pos.BOTTOM_LEFT, 0, 5);
        Button regBtn = getButton(gridPane, "Register", Pos.BOTTOM_RIGHT, 1, 5);

        //Set text
        final Text actionTarget = getText(gridPane, TextAlignment.RIGHT, 1, 6);

        regBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (userTextField.getText().length() < 3) actionTarget.setText("Username is too short!");
                else if (pwBox.getText().length() < 3) actionTarget.setText("Password is too short!");
                else if (!pwBox.getText().equals(pwBox2.getText())) actionTarget.setText("The passwords are not the same!");
                else {
                    try {
                        HanRoutingProtocol.ClientRegisterResponse.Status status = gui.pLayer.register(userTextField.getText(), pwBox.getText());
                        if (status == status.SUCCES) actionTarget.setText("Registration successful!");
                        else if (status == status.FAILED) actionTarget.setText("Error!");
                        else if (status == status.TAKEN_USERNAME) actionTarget.setText("Error! Chose another username.");
                    } catch (Exception ex) {
                        actionTarget.setText("Something went terribly wrong!");
                    }
                }
            }
        });

        backBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                gui.setStage(GUI.Page.LOGIN);
            }
        });
    }
 
    public GridPane getGridPane() {
        return gridPane;
    }
}
