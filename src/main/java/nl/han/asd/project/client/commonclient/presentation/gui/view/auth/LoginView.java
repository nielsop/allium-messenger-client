package nl.han.asd.project.client.commonclient.presentation.gui.view.auth;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import nl.han.asd.project.client.commonclient.presentation.gui.PaneFactory;
import nl.han.asd.project.client.commonclient.presentation.gui.view.Styles;

/**
 * Created by Marius on 19-04-16.
 */
public class LoginView {
    private GridPane gridPane;
    private JFXButton registerButton;
    private JFXButton loginButton;
    private JFXTextField usernameField;
    private JFXPasswordField passwordField;
    private Text status;

    public LoginView() {
        //Set gridPane
        gridPane = PaneFactory.getGridPane(Pos.CENTER, new int[] { 25, 25, 25, 25 });

        Text title = new Text("Welcome to the onion messenger");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        //Set labels
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");

        //Set input fields
        usernameField = new JFXTextField();
        passwordField = new JFXPasswordField();

        //Set buttons
        registerButton = new JFXButton("Register");
        registerButton.setStyle(Styles.FX_BUTTON_RAISED);
        registerButton.setAlignment(Pos.BOTTOM_LEFT);
        loginButton = new JFXButton("Login");
        loginButton.setStyle(Styles.FX_BUTTON_RAISED);
        loginButton.setAlignment(Pos.BOTTOM_RIGHT);

        //Set text
        status = new Text();

        gridPane.add(title, 0, 0, 2, 1);
        gridPane.add(usernameLabel, 0, 1);
        gridPane.add(usernameField, 1, 1);
        gridPane.add(passwordLabel, 0, 2);
        gridPane.add(passwordField, 1, 2);
        gridPane.add(registerButton, 0, 4);
        gridPane.add(loginButton, 1, 4);
        gridPane.add(status, 1, 5);
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    public JFXButton getRegisterButton() {
        return registerButton;
    }

    public JFXButton getLoginButton() {
        return loginButton;
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public String getStatus() {
        return status.getText();
    }

    public void setStatus(String text) {
        status.setText(text);
    }
}
