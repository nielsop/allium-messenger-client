package nl.han.asd.project.client.commonclient.login;

import com.google.inject.Inject;
import nl.han.asd.project.client.commonclient.master.IAuthentication;
import nl.han.asd.project.client.commonclient.master.MasterGateway;
import nl.han.asd.project.client.commonclient.node.ISetConnectedNodes;
import nl.han.asd.project.protocol.HanRoutingProtocol;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 18-4-2016
 */
public class LoginService implements ILogin {

    private static final MasterGateway masterGateway = new MasterGateway(null);
    private static final String REGEX_ALPHANUMERIC = "[a-zA-Z0-9]";
    private static final String REGEX_ALPHANUMERICSPECIAL = "^(?=(?:\\D*?\\d){8,32}(?!.*?\\d))[a-zA-Z0-9@\\#$%&*()_+\\]\\[';:?.,!^-]+$";

    private ISetConnectedNodes setConnectedNodes;
    private IAuthentication authentication;

    @Inject
    public LoginService(ISetConnectedNodes setConnectedNodes, IAuthentication authentication) {
        this.setConnectedNodes = setConnectedNodes;
        this.authentication = authentication;
    }

    public LoginService() {

    }

    public boolean validateLoginData(String username, String password) {
        if (username == null || password == null)
            throw new IllegalArgumentException("De ingevoerde username en password mogen niet null zijn!");
        if (username.isEmpty() || password.isEmpty())
            throw new IllegalArgumentException("De ingevoerde username en password mogen niet leeg zijn!");
        if (!username.matches(REGEX_ALPHANUMERIC))
            throw new IllegalArgumentException("De ingevoerde username mag alleen letters" +
                    " en cijfers bevatten!");
        if (!password.matches(REGEX_ALPHANUMERICSPECIAL)) {
            throw new IllegalArgumentException("De ingevoerde password mag alleen letters," +
                    " cijfers en speciale tekens bevatten!");
        }
        if (username.length() < 3) throw new IllegalArgumentException("De username moet minstens 3 tekens bevatten!");
        if (username.length() > 12) throw new IllegalArgumentException("De username mag maximaal 12 tekens bevatten!");
        if (password.length() < 8)
            throw new IllegalArgumentException("Het wachtwoord moet minstens 8 tekens bevatten!");
        if (password.length() > 16)
            throw new IllegalArgumentException("Het wachtwoord mag maximaal 16 tekens bevatten!");
        return true;
    }

    public boolean login(String username, String password) {
        //TODO: Initialize setConnectedNodes upon successful authentication
        return validateLoginData(username, password) &&
                masterGateway.authenticate(username, password).status ==
                        HanRoutingProtocol.ClientLoginResponse.Status.SUCCES;
    }
}
