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
import nl.han.asd.project.client.commonclient.presentation.gui.view.Pane;

public class LoginView {
    public Button registerButton;
    public Button loginButton;
    public TextField usernameField;
    public PasswordField passwordField;
    public Text status;
    GridPane gridPane = null;

    public LoginView() {
        gridPane = Pane.getGridPane(Pos.CENTER, new int[] { 25, 25, 25, 25 });
        setTitle();
        createLabels();
        setInputFields();
        setButtons();
        setText();
    }

    private void setTitle() {
        Text title = new Text("Welcome to the onion messenger");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        gridPane.add(title, 0, 0, 2, 1);
    }

    private void createLabels() {
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
}
