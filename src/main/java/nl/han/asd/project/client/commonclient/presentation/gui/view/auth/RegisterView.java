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
public class RegisterView {
    private JFXTextField usernameField;
    private JFXPasswordField passwordField;
    private JFXPasswordField passwordRepeatField;
    private JFXButton cancelButton;
    private JFXButton registerButton;
    private Text status;
    private GridPane gridPane;


    public RegisterView() {
        //Set gridPane
        gridPane = PaneFactory.getGridPane(Pos.CENTER, new int[]{25, 25, 25, 25});
        setTitle();
        setLabels();
        setInputFields();
        setButtons();
        setText();
    }

    private void setInputFields() {
        usernameField = new JFXTextField();
        passwordField = new JFXPasswordField();
        passwordRepeatField = new JFXPasswordField();
        gridPane.add(usernameField, 1, 1);
        gridPane.add(passwordField, 1, 2);
        gridPane.add(passwordRepeatField, 1, 3);
    }

    private void setButtons() {
        cancelButton = new JFXButton("Cancel");
        cancelButton.setStyle(Styles.FX_BUTTON_RAISED);
        cancelButton.setAlignment(Pos.BOTTOM_LEFT);
        registerButton = new JFXButton("Register");
        registerButton.setStyle(Styles.FX_BUTTON_RAISED);
        registerButton.setAlignment(Pos.BOTTOM_RIGHT);
        gridPane.add(cancelButton, 0, 5);
        gridPane.add(registerButton, 1, 5);
    }

    private void setText() {
        status = new Text();
        gridPane.add(status, 1, 6);
    }

    private void setLabels() {
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        Label passwordRepeatLabel = new Label("Password repeat:");
        gridPane.add(usernameLabel, 0, 1);
        gridPane.add(passwordLabel, 0, 2);
        gridPane.add(passwordRepeatLabel, 0, 3);
    }

    private void setTitle() {
        Text title = new Text("Welcome to the onion messenger");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        gridPane.add(title, 0, 0, 2, 1);
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public JFXButton getRegisterButton() {
        return registerButton;
    }

    public JFXButton getCancelButton() {
        return cancelButton;
    }

    public String getPasswordRepeat() {
        return passwordRepeatField.getText();
    }

    public String getStatus() {
        return status.getText();
    }

    public void setStatus(String text) {
        status.setText(text);
    }

    public GridPane getGridPane() {
        return gridPane;
    }
}
