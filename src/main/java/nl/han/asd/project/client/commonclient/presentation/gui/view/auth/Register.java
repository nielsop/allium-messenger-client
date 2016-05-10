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
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.presentation.gui.view.Pane;
import nl.han.asd.project.protocol.HanRoutingProtocol;

public class Register {
    private final Button registerButton = new Button("Register");
    private final Text status = new Text();
    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final PasswordField passwordRepeatField = new PasswordField();
    private final Button cancelButton = new Button("Cancel");
    private GridPane gridPane = null;
    private GUI gui;

    public Register(GUI gui) {
        this.gui = gui;
        gridPane = Pane.getGridPane(Pos.CENTER, new int[] { 25, 25, 25, 25 });
        setTitle();
        setLabels();
        setInputFields();
        setButtonText();
        setText();
        registerButton.setOnAction(getOnRegisterButtonEventHandler());
        cancelButton.setOnAction(getOnCancelButtonEventHandler());
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    private void setTitle() {
        Text title;
        title = new Text("Welcome to the onion messenger");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        gridPane.add(title, 0, 0, 2, 1);
    }

    private void setLabels() {
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        Label passwordRepeatLabel = new Label("Password repeat:");

        gridPane.add(usernameLabel, 0, 1);
        gridPane.add(passwordLabel, 0, 2);
        gridPane.add(passwordRepeatLabel, 0, 3);
    }

    private void setInputFields() {
        gridPane.add(usernameField, 1, 1);
        gridPane.add(passwordField, 1, 2);
        gridPane.add(passwordRepeatField, 1, 3);
    }

    private void setButtonText() {
        cancelButton.setAlignment(Pos.BOTTOM_LEFT);
        registerButton.setAlignment(Pos.BOTTOM_RIGHT);
        gridPane.add(cancelButton, 0, 5);
        gridPane.add(registerButton, 1, 5);
    }

    private void setText() {
        gridPane.add(status, 1, 6);
    }

    private EventHandler<ActionEvent> getOnCancelButtonEventHandler() {
        return e -> gui.setStage(GUI.Page.LOGIN);
    }

    private EventHandler<ActionEvent> getOnRegisterButtonEventHandler() {
        return e -> {
            if (usernameField.getText().length() < 3) {
                status.setText("Username is too short!");
            } else if (passwordField.getText().length() < 3) {
                status.setText("Password is too short!");
            } else if (!passwordField.getText().equals(passwordRepeatField.getText())) {
                status.setText("The passwords are not the same!");
            } else {
                HanRoutingProtocol.ClientRegisterResponse.Status registerResponseStatus = gui.pLayer
                        .register(usernameField.getText(), passwordField.getText());
                status.setText(getStatusText(registerResponseStatus));
            }
        };
    }

    private String getStatusText(HanRoutingProtocol.ClientRegisterResponse.Status status) {
        switch (status) {
            case SUCCES:
                return "Registration successful!";
            case FAILED:
                return "Error!";
            case TAKEN_USERNAME:
                return "Error! Chose another username.";
            default:
                return "Something went terribly wrong!";
        }
    }
}
