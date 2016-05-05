package nl.han.asd.project.client.commonclient.login;

import nl.han.asd.project.client.commonclient.master.IAuthentication;
import nl.han.asd.project.client.commonclient.master.MasterGateway;
import nl.han.asd.project.client.commonclient.master.wrapper.LoginResponseWrapper;
import nl.han.asd.project.client.commonclient.node.ISetConnectedNodes;
import nl.han.asd.project.client.commonclient.utility.Validation;

import javax.inject.Inject;

/**
 * Created by Marius on 19-04-16.
 */
public class LoginService implements ILogin {

    private MasterGateway masterGateway = null;

    private ISetConnectedNodes setConnectedNodes;
    private IAuthentication authentication;

    @Inject
    public LoginService(ISetConnectedNodes setConnectedNodes, IAuthentication authentication, MasterGateway gateway) {
        this.setConnectedNodes = setConnectedNodes;
        this.authentication = authentication;
        this.masterGateway = gateway;
    }

    public LoginResponseWrapper login(String username, String password) {
        // DO NOT REMOVE, YET!
        if (Validation.validateLoginData(username, password)) return masterGateway.authenticate(username, password);
        else return null;

        //TODO: Initialize setConnectedNodes upon successful authentication
//        return Validation.validateLoginData(username, password) &&
//                masterGateway.authenticate(username, password).status ==
//                        HanRoutingProtocol.ClientLoginResponse.Status.SUCCES;
    }
}
