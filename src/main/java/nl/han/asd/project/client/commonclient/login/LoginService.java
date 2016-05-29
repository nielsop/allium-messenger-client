package nl.han.asd.project.client.commonclient.login;

import java.io.IOException;

import javax.inject.Inject;

import com.google.protobuf.ByteString;

import nl.han.asd.project.client.commonclient.connection.MessageNotSentException;
import nl.han.asd.project.client.commonclient.master.IAuthentication;
import nl.han.asd.project.client.commonclient.store.CurrentUser;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
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
    private IEncryptionService encryptionService;

    /**
     * Construct a new LoginService.
     *
     * @param authentication the authentication interface
     * @param encryptionService the encryptionservice holding
     *          the public key
     *
     * @throws IllegalArgumentException if authentication
     *          or encryptionService is null
     */
    @Inject
    public LoginService(IAuthentication authentication, IEncryptionService encryptionService) {
        this.authentication = Check.notNull(authentication, "authentication");
        this.encryptionService = Check.notNull(encryptionService, "encryptionService");
    }

    /** {@inheritDoc} */
    @Override
    public CurrentUser login(String username, String password)
            throws InvalidCredentialsException, IOException, MessageNotSentException {
        UserCheck.checkUsername(username);
        UserCheck.checkPassword(password);

        Builder loginRequest = ClientLoginRequest.newBuilder();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);
        loginRequest.setPublicKey(ByteString.copyFrom(encryptionService.getPublicKey()));

        ClientLoginResponse loginResponse = authentication.login(loginRequest.build());

        if (loginResponse.getStatus() != ClientLoginResponse.Status.SUCCES) {
            throw new InvalidCredentialsException(loginResponse.getStatus().name());
        }

        return new CurrentUser(username, encryptionService.getPublicKey(), loginResponse.getSecretHash());
    }
}
