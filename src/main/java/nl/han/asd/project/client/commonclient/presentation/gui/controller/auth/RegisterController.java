package nl.han.asd.project.client.commonclient.presentation.gui.controller.auth;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.GridPane;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.presentation.gui.model.auth.RegisterModel;
import nl.han.asd.project.client.commonclient.presentation.gui.view.auth.RegisterView;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Marius on 10-05-16.
 */
public class RegisterController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);
    private RegisterView view;
    private RegisterModel model;

    public RegisterController(GUI gui) {
        view = new RegisterView();
        model = new RegisterModel(gui);
        onActions();
    }

    private void onActions() {
        view.getRegisterButton().setOnAction(getOnRegisterButtonAction());
        view.getCancelButton().setOnAction(e -> model.setStage(GUI.Page.LOGIN));
    }

    private EventHandler<ActionEvent> getOnRegisterButtonAction() {
        return e -> {
            if (validateInput()) {
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
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        };
    }

    private boolean validateInput() {
        if (view.getUsername().length() < 3) {
            view.setStatus("Username is too short!");
            return false;
        } else if (view.getPassword().length() < 3) {
            view.setStatus("Password is too short!");
            return false;
        } else if (!view.getPassword().equals(view.getPasswordRepeat())) {
            view.setStatus("The passwords are not the same!");
            return false;
        }
        return true;
    }

    public GridPane getGridPane() {
        return view.getGridPane();
    }
}
