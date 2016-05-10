package nl.han.asd.project.client.commonclient.presentation.gui.model.auth;

import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.protocol.HanRoutingProtocol;

/**
 * Created by Marius on 10-05-16.
 */
public class RegisterModel {
    private GUI gui;

    public RegisterModel(GUI gui) {
        this.gui = gui;
    }

    public HanRoutingProtocol.ClientRegisterResponse.Status getRegisterStatus(String username, String password) {
        return gui.pLayer.register(username, password);
    }
}
