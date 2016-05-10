package nl.han.asd.project.client.commonclient.presentation.gui.controller.auth;

import javafx.scene.layout.GridPane;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.presentation.gui.model.auth.LoginModel;
import nl.han.asd.project.client.commonclient.presentation.gui.view.auth.LoginView;
import nl.han.asd.project.protocol.HanRoutingProtocol;

/**
 * Created by Kenny on 9-5-2016.
 */
public class LoginController {
    private LoginView view;
    private LoginModel model;

    public LoginController(GUI gui) {
        view = new LoginView();
        model = new LoginModel(gui);
        onActions();
    }

    private void onActions() {
        view.getLoginButton().setOnAction(e -> {
            if (view.getUsername().length() < 3)
                view.setStatus("Username is too short!");
            else if (view.getPassword().length() < 8)
                view.setStatus("Password is too short!");
            else {
                if (model.getLoginStatus(view.getUsername(), view.getPassword()) == HanRoutingProtocol.ClientLoginResponse.Status.SUCCES) {
                    model.setStage(GUI.Page.DASHBOARD);
                }
            }
        });

        view.getRegisterButton().setOnAction(e -> model.setStage(GUI.Page.REGISTER));
    }

    public GridPane getGridPane() {
        return view.getGridPane();
    }

}
