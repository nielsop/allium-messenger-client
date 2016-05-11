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

    public LoginController(GUI gui) {
        view = new LoginView();
        model = new LoginModel(gui);
        onActions();
    }

    private void onActions() {
        view.getLoginButton().setOnAction(e -> {
            if (view.getUsername().length() < 3)
                view.setStatus("Username is too short! At least 3 characters.");
            else if (view.getPassword().length() < 8)
                view.setStatus("Password is too short! At least 8 characters.");
            else {
                switch(model.getLoginStatus(view.getUsername(), view.getPassword())) {
                    case SUCCES:
                        setStage(GUI.Page.DASHBOARD);
                        break;
                    case INVALID_COMBINATION:
                        view.setStatus("Username or password is incorrect!");
                        break;
                    case FAILED:
                        view.setStatus("Error while logging in, please try again!");
                        break;
                }
            }
        });

        view.getRegisterButton().setOnAction(e -> setStage(GUI.Page.REGISTER));
    }

    public GridPane getGridPane() {
        return view.getGridPane();
    }

    public void setStage(GUI.Page stage) {
        model.getGUI().setScene(stage);
    }

}
