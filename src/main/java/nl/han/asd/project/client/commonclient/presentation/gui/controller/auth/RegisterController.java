package nl.han.asd.project.client.commonclient.presentation.gui.controller.auth;

import javafx.scene.layout.GridPane;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.presentation.gui.model.auth.RegisterModel;
import nl.han.asd.project.client.commonclient.presentation.gui.view.auth.RegisterView;
import nl.han.asd.project.protocol.HanRoutingProtocol;

/**
 * Created by Marius on 10-05-16.
 */
public class RegisterController {
    RegisterView view;
    RegisterModel model;
    private GUI gui;

    public RegisterController(GUI gui) {
        this.gui = gui;
        view = new RegisterView();
        model = new RegisterModel(gui);
        onActions();
    }

    private void onActions() {
        view.getRegisterButton().setOnAction(e -> {
            if (view.getUsername().length() < 3)
                view.setStatus("Username is too short!");
            else if (view.getPassword().length() < 3)
                view.setStatus("Password is too short!");
            else if (!view.getPassword().equals(view.getPasswordRepeat()))
                view.setStatus("The passwords are not the same!");
            else {
                try {
                    HanRoutingProtocol.ClientRegisterResponse.Status registerResponseStatus = model.getRegisterStatus(view.getUsername(), view.getPassword());
                    if (registerResponseStatus == registerResponseStatus.SUCCES)
                        view.setStatus("Registration successful!");
                    else if (registerResponseStatus == registerResponseStatus.FAILED)
                        view.setStatus("Error!");
                    else if (registerResponseStatus == registerResponseStatus.TAKEN_USERNAME)
                        view.setStatus("Error! Chose another username.");
                } catch (Exception ex) {
                    view.setStatus("Something went terribly wrong!");
                }
            }
        });

        view.getCancelButton().setOnAction(e -> gui.setStage(GUI.Page.LOGIN));
    }

    public GridPane getGridPane() {
        return view.getGridPane();
    }
}
