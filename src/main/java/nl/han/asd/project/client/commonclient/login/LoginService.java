package nl.han.asd.project.client.commonclient.login;

import nl.han.asd.project.client.commonclient.master.IAuthentication;
import nl.han.asd.project.client.commonclient.master.MasterGateway;
import nl.han.asd.project.client.commonclient.master.wrapper.LoginResponseWrapper;
import nl.han.asd.project.client.commonclient.node.ISetConnectedNodes;
import nl.han.asd.project.client.commonclient.utility.Validation;

import javax.inject.Inject;

public class LoginService implements ILoginService {

    private static final String REGEX_ALPHANUMERIC = "[a-zA-Z0-9]";
    private static final String REGEX_ALPHANUMERICSPECIAL = "^(?=(?:\\D*?\\d){8,32}(?!.*?\\d))[a-zA-Z0-9@\\#$%&*()_+\\]\\[';:?.,!^-]+$";

    private MasterGateway masterGateway;
    private ISetConnectedNodes setConnectedNodes;
    private IAuthentication authentication;

    /**
     * Creates a loginService object using guice dependency injection.
     *
     * @param setConnectedNodes interface IGetConnectedNodes.
     * @param authentication    interface IAuthentication.
     * @param gateway           MasterGateway gateway.
     */
    @Inject
    public LoginService(ISetConnectedNodes setConnectedNodes, IAuthentication authentication, MasterGateway gateway) {
        this.setConnectedNodes = setConnectedNodes;
        this.authentication = authentication;
        this.masterGateway = gateway;
    }

    /**
     * Creates a LoginResponseWrapper using a username and a password.
     *
     * @param username the username to login.
     * @param password the password to login.
     * @return LoginResponseWrapper as a result from MasterGateway.register(username, password).
     */
    @Override
    public LoginResponseWrapper login(String username, String password) {
        // DO NOT REMOVE, YET!
        if (Validation.validateCredentials(username, password))
            return masterGateway.authenticate(username, password);
        else
            return null;
    }

    @Override
    public boolean logout(String username, String secretHash) {
        return masterGateway.logout(username, secretHash);
    }
}
