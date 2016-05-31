package nl.han.asd.project.client.commonclient.login;

import com.google.protobuf.ByteString;
import nl.han.asd.project.client.commonclient.connection.MessageNotSentException;
import nl.han.asd.project.client.commonclient.master.IAuthentication;
import nl.han.asd.project.client.commonclient.master.MasterGateway;
import nl.han.asd.project.client.commonclient.store.CurrentUser;
import nl.han.asd.project.client.commonclient.utility.Validation;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.commonservices.internal.utility.Check;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Provide the methods used to login.
 *
 * @version 1.0
 */
public class LoginService implements ILoginService {
    public static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);
    private MasterGateway masterGateway;
    private IAuthentication authentication;
    private IEncryptionService encryptionService;

    /**
     * Construct a new LoginService.
     *
     * @param authentication    the authentication interface
     * @param encryptionService the encryptionservice holding
     *                          the public key
     * @throws IllegalArgumentException if authentication
     *                                  or encryptionService is null
     */
    @Inject
    public LoginService(IAuthentication authentication, IEncryptionService encryptionService, MasterGateway masterGateway) {
        this.authentication = Check.notNull(authentication, "authentication");
        this.encryptionService = Check.notNull(encryptionService, "encryptionService");
        this.masterGateway = Check.notNull(masterGateway, "masterGateway");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CurrentUser login(String username, String password) throws InvalidCredentialsException, IOException, MessageNotSentException {
        try {
            Validation.validateCredentials(username, password);

            HanRoutingProtocol.ClientLoginRequest.Builder loginRequest = HanRoutingProtocol.ClientLoginRequest.newBuilder();
            loginRequest.setUsername(username);
            loginRequest.setPassword(password);
            loginRequest.setPublicKey(ByteString.copyFrom(encryptionService.getPublicKey()));

            HanRoutingProtocol.ClientLoginResponse loginResponse = authentication.login(loginRequest.build());

            if (loginResponse.getStatus() != HanRoutingProtocol.ClientLoginResponse.Status.SUCCES) {
                throw new InvalidCredentialsException(loginResponse.getStatus().name());
            }

            return new CurrentUser(username, encryptionService.getPublicKey(), loginResponse.getSecretHash());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
    }

    public boolean logout(String username, String secretHash) {
        return masterGateway.logout(username, secretHash);
    }
}
