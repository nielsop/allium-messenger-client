package nl.han.asd.project.client.commonclient.presentation.gui.controller.auth;

import javafx.scene.layout.GridPane;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.presentation.gui.model.auth.LoginModel;
import nl.han.asd.project.client.commonclient.presentation.gui.view.auth.LoginView;

/**
 * Created by Kenny on 9-5-2016.
 */
public class LoginController {
    private static final String ONSUBMIT_PASS_SHORT = "Password is too short! At least 8 characters.";
    private static final String ONSUBMIT_USERNAME_SHORT = "Username is too short! At least 3 characters.";
    private static final String ONLOGIN_INVALID_COMBINATION = "Username or password is incorrect!";
    private static final String ONLOGIN_FAILED = "Error while logging in, please try again!";
    private static final String ONLOGIN_DEFAULT = "Error while logging in, please try again!";
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
                view.setStatus(ONSUBMIT_USERNAME_SHORT);
            else if (view.getPassword().length() < 8)
                view.setStatus(ONSUBMIT_PASS_SHORT);
            else {
                onLogin();
            }
        });

        view.getRegisterButton().setOnAction(e -> setStage(GUI.Page.REGISTER));
    }

    private void onLogin() {
        switch(model.getLoginStatus(view.getUsername(), view.getPassword())) {
            case SUCCES:
                setStage(GUI.Page.DASHBOARD);
                break;
            case INVALID_COMBINATION:
                view.setStatus(ONLOGIN_INVALID_COMBINATION);
                break;
            case FAILED:
                view.setStatus(ONLOGIN_FAILED);
                break;
            default:
                view.setStatus(ONLOGIN_DEFAULT);
                break;
        }
    }

    public GridPane getGridPane() {
        return view.getGridPane();
    }

    public void setStage(GUI.Page stage) {
        model.getGUI().setScene(stage);
    }

}
