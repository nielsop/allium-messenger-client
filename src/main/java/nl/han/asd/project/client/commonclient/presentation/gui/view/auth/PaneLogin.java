package nl.han.asd.project.client.commonclient.presentation.gui.view.auth;

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

/**
 * Created by Marius on 19-04-16.
 */
public class PaneLogin {
    GridPane gridPane = null;

    public PaneLogin(GUI gui) {
        //Set gridPane
        gridPane = Pane.getGridPane(Pos.CENTER, new int[]{25, 25, 25, 25});

        Text title = new Text("Welcome to the onion messenger");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        //Set labels
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");

        //Set input fields
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();

        //Set buttons
        Button registerButton = new Button("Register");
        registerButton.setAlignment(Pos.BOTTOM_LEFT);
        Button loginButton = new Button("Login");
        loginButton.setAlignment(Pos.BOTTOM_RIGHT);

        //Set text
        Text status = new Text();

        gridPane.add(title, 0, 0, 2, 1);
        gridPane.add(usernameLabel, 0, 1);
        gridPane.add(usernameField, 1, 1);
        gridPane.add(passwordLabel, 0, 2);
        gridPane.add(passwordField, 1, 2);
        gridPane.add(registerButton, 0, 4);
        gridPane.add(loginButton, 1, 4);
        gridPane.add(status, 1, 5);

        loginButton.setOnAction(e -> {
//                if (userTextField.getText().length() < 3) actionTarget.setText("Username is too short!");
//                else if (pwBox.getText().length() < 3) actionTarget.setText("Password is too short!");
//                else
            gui.setStage(GUI.Page.DASHBOARD);
        });

        registerButton.setOnAction(e -> gui.setStage(GUI.Page.REGISTER));
    }

    public GridPane getGridPane() {
        return gridPane;
    }
}
