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
import nl.han.asd.project.client.commonclient.presentation.gui.PaneFactory;

public class LoginView {
    private GridPane gridPane;
    private Button registerButton;
    private Button loginButton;
    private TextField usernameField;
    private PasswordField passwordField;
    private Text status;

    public LoginView() {
        gridPane = PaneFactory.getGridPane(Pos.CENTER, new int[] { 25, 25, 25, 25 });
        setTitle();
        setLabels();
        setInputFields();
        setButtons();
        setText();
    }

    private void setTitle() {
        Text title = new Text("Welcome to the onion messenger");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        gridPane.add(title, 0, 0, 2, 1);
    }

    private void setLabels() {
        gridPane.add(new Label("Username:"), 0, 1);
        gridPane.add(new Label("Password:"), 0, 2);
    }

    private void setInputFields() {
        usernameField = new TextField();
        passwordField = new PasswordField();
        gridPane.add(usernameField, 1, 1);
        gridPane.add(passwordField, 1, 2);
    }

    private void setButtons() {
        registerButton = new Button("Register");
        registerButton.setAlignment(Pos.BOTTOM_LEFT);
        loginButton = new Button("Login");
        loginButton.setAlignment(Pos.BOTTOM_RIGHT);

        gridPane.add(registerButton, 0, 4);
        gridPane.add(loginButton, 1, 4);
    }

    private void setText() {
        status = new Text();
        gridPane.add(status, 1, 5);
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    public Button getRegisterButton() {
        return registerButton;
    }

    public Button getLoginButton() {
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
