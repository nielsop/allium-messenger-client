package nl.han.asd.project.client.commonclient.presentation.gui.controller.auth;

import javafx.scene.layout.GridPane;
import nl.han.asd.project.client.commonclient.presentation.PresentationLayer;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.presentation.gui.model.auth.RegisterModel;
import nl.han.asd.project.client.commonclient.presentation.gui.view.auth.RegisterView;
import nl.han.asd.project.protocol.HanRoutingProtocol;

/**
 * Created by Marius on 10-05-16.
 */
public class RegisterController {
    private static final String ONREGISTER_DEFAULT = "Something went wrong (default)";
    private static final String ONREGISTER_TAKEN_USERNAME = "Error! Chose another username.";
    private static final String ONREGISTER_FAILED = "Error!";
    private static final String ONREGISTER_SUCCES = "Registration successful!";
    private static final String ONSUBMIT_USERNAME_SHORT = "Username is too short!";
    private static final String ONSUBMIT_PASS_SHORT = "Password is too short!";
    private static final String ONSUBMIT_PASS_DIFFERENT = "The passwords are not the same!";
    private RegisterView view;
    private RegisterModel model;

    public RegisterController(GUI gui) {
        view = new RegisterView();
        model = new RegisterModel(gui);
        onActions();
    }

    private void onActions() {
        view.getRegisterButton().setOnAction(e -> {
            if (view.getUsername().length() < 3)
                view.setStatus(ONSUBMIT_USERNAME_SHORT);
            else if (view.getPassword().length() < 3)
                view.setStatus(ONSUBMIT_PASS_SHORT);
            else if (!view.getPassword().equals(view.getPasswordRepeat()))
                view.setStatus(ONSUBMIT_PASS_DIFFERENT);
            else {
                try {
                    onRegister();
                } catch (Exception ex) {
                    PresentationLayer.LOGGER.error(ex.getMessage(), ex);
                    view.setStatus(ONREGISTER_FAILED);
                }
            }
        });

        view.getCancelButton().setOnAction(e -> model.setStage(GUI.Page.LOGIN));
    }

    private void onRegister() {
        HanRoutingProtocol.ClientRegisterResponse.Status registerResponseStatus = model.getRegisterStatus(view.getUsername(), view.getPassword());
        switch (registerResponseStatus) {
            case SUCCES:
                view.setStatus(ONREGISTER_SUCCES);
                break;
            case FAILED:
                view.setStatus(ONREGISTER_FAILED);
                break;
            case TAKEN_USERNAME:
                view.setStatus(ONREGISTER_TAKEN_USERNAME);
                break;
            default:
                view.setStatus(ONREGISTER_DEFAULT);
                break;
        }
    }

    public GridPane getGridPane() {
        return view.getGridPane();
    }
}
