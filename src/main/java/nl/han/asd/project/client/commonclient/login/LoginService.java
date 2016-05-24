package nl.han.asd.project.client.commonclient.login;

import java.io.IOException;

import javax.inject.Inject;

import nl.han.asd.project.client.commonclient.connection.MessageNotSendException;
import nl.han.asd.project.client.commonclient.master.IAuthentication;
import nl.han.asd.project.client.commonclient.node.ISetConnectedNodes;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.commonservices.internal.utility.Check;
import nl.han.asd.project.protocol.HanRoutingProtocol.ClientLoginRequest;
import nl.han.asd.project.protocol.HanRoutingProtocol.ClientLoginRequest.Builder;
import nl.han.asd.project.protocol.HanRoutingProtocol.ClientLoginResponse;

/**
 * Provide the methods used to login.
 *
 * @version 1.0
 */
public class LoginService implements ILogin {

    private IAuthentication authentication;

    /**
     * Construct a new LoginService.
     *
     * @param authentication the authentication interface
     */
    @Inject
    public LoginService(ISetConnectedNodes setConnectedNodes, IAuthentication authentication) {
        this.authentication = Check.notNull(authentication, "authentication");
    }

    /** {@inheritDoc} */
    @Override
    public Contact login(String username, String password)
            throws InvalidCredentialsException, IOException, MessageNotSendException {
        UserCheck.checkUsername(username);
        UserCheck.checkPassword(password);

        Builder loginRequest = ClientLoginRequest.newBuilder();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        ClientLoginResponse loginResponse = authentication.login(loginRequest.build());

        if (loginResponse.getStatus() != ClientLoginResponse.Status.SUCCES) {
            throw new InvalidCredentialsException(loginResponse.getStatus().name());
        }

        Contact currentUser = new Contact(username, null);
        currentUser.setSecretHash(loginResponse.getSecretHash());
        return currentUser;
    }
}
