package nl.han.asd.project.client.commonclient.presentation.gui.model.auth;

import com.amazonaws.services.elasticache.model.SourceType;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.protocol.HanRoutingProtocol;

/**
 * Created by Kenny on 9-5-2016.
 */
public class LoginModel {
    private GUI gui;

    public LoginModel(GUI gui) {
        this.gui = gui;
    }

    public HanRoutingProtocol.ClientLoginResponse.Status getLoginStatus(String username, String password) {
        return gui.pLayer.login(username, password);
    }

    public void setStage(GUI.Page stage) {
        gui.setStage(stage);
    }
}
