package nl.han.asd.project.client.commonclient.presentation.gui.controller.auth;

import javafx.scene.layout.GridPane;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.presentation.gui.model.auth.LoginModel;
import nl.han.asd.project.client.commonclient.presentation.gui.view.auth.LoginView;

/**
 * Created by Kenny on 9-5-2016.
 */
public class LoginController {
    private LoginView view;
    private LoginModel model;
    private GUI gui;

    public LoginController(GUI gui) {
        this.gui = gui;
        view = new LoginView();
        model = new LoginModel(gui);
        onActions();
    }

    private void onActions() {
        view.loginButton.setOnAction(e -> {
            if (view.usernameField.getText().length() < 3)
                view.status.setText("Username is too short!");
            else if (view.passwordField.getText().length() < 8)
                view.status.setText("Password is too short!");
            else {
                if (model.isLoginSuccess(view.usernameField.getText(), view.passwordField.getText())) {
                    gui.setStage(GUI.Page.DASHBOARD);
                }
            }
        });

        view.registerButton.setOnAction(e -> gui.setStage(GUI.Page.REGISTER));
    }

    public GridPane getGridPane() {
        return view.getGridPane();
    }

}
