package nl.han.asd.project.client.commonclient.presentation.gui.view.auth;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.presentation.gui.view.Pane;
import nl.han.asd.project.protocol.HanRoutingProtocol;

/**
 * Created by Marius on 19-04-16.
 */
public class PaneRegister {
    GridPane gridPane = null;

    public PaneRegister(GUI gui) {
        //Set gridPane
        gridPane = Pane.getGridPane(Pos.CENTER, new int[]{25, 25, 25, 25});
        Text title = new Text("Welcome to the onion messenger");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        Label passwordRepeatLabel = new Label("Password repeat:");

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        PasswordField passwordRepeatField = new PasswordField();

        Button cancelButton = new Button("Cancel");
        cancelButton.setAlignment(Pos.BOTTOM_LEFT);
        Button registerButton = new Button("Register");
        registerButton.setAlignment(Pos.BOTTOM_RIGHT);

        Text status = new Text();

        gridPane.add(title, 0, 0, 2, 1);
        gridPane.add(usernameLabel, 0, 1);
        gridPane.add(passwordLabel, 0, 2);
        gridPane.add(passwordRepeatLabel, 0, 3);
        gridPane.add(usernameField, 1, 1);
        gridPane.add(passwordField, 1, 2);
        gridPane.add(passwordRepeatField, 1, 3);
        gridPane.add(cancelButton, 0, 5);
        gridPane.add(registerButton, 1, 5);
        gridPane.add(status, 1, 6);

        registerButton.setOnAction(e -> {
            if (usernameField.getText().length() < 3) status.setText("Username is too short!");
            else if (passwordField.getText().length() < 3) status.setText("Password is too short!");
            else if (!passwordField.getText().equals(passwordRepeatField.getText())) status.setText("The passwords are not the same!");
            else {
                try {
                    HanRoutingProtocol.ClientRegisterResponse.Status registerResponseStatus = gui.pLayer.register(usernameField.getText(), passwordField.getText());
                    if (registerResponseStatus == registerResponseStatus.SUCCES) status.setText("Registration successful!");
                    else if (registerResponseStatus == registerResponseStatus.FAILED) status.setText("Error!");
                    else if (registerResponseStatus == registerResponseStatus.TAKEN_USERNAME) status.setText("Error! Chose another username.");
                } catch (Exception ex) {
                    status.setText("Something went terribly wrong!");
                }
            }
        });

        cancelButton.setOnAction(e -> gui.setStage(GUI.Page.LOGIN));
    }
 
    public GridPane getGridPane() {
        return gridPane;
    }
}
