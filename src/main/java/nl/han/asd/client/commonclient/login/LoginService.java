package nl.han.asd.client.commonclient.login;

import nl.han.asd.client.commonclient.master.IAuthentication;
import nl.han.asd.client.commonclient.node.ISetConnectedNodes;

import javax.inject.Inject;

/**
 * Created by Marius on 19-04-16.
 */
public class LoginService implements ILogin {
    private ISetConnectedNodes setConnectedNodes;
    private IAuthentication authentication;

    @Inject
    public LoginService(ISetConnectedNodes setConnectedNodes, IAuthentication authentication) {
        this.setConnectedNodes = setConnectedNodes;
        this.authentication = authentication;
    }
}
