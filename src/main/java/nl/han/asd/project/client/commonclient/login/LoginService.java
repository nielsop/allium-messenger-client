package nl.han.asd.project.client.commonclient.login;

import nl.han.asd.project.client.commonclient.master.IAuthentication;
import nl.han.asd.project.client.commonclient.master.MasterGateway;
import nl.han.asd.project.protocol.HanRoutingProtocol;

/**
 * Created by DDulos on 13-Apr-16.
 */
public class LoginService implements ILogin {

    private static final MasterGateway masterGateway = new MasterGateway();

    private static final String REGEX_ALPHANUMERIC = "[a-zA-Z0-9]";
    private static final String REGEX_ALPHANUMERICSPECIAL = "^(?=(?:\\D*?\\d){8,32}(?!.*?\\d))[a-zA-Z0-9@\\#$%&*()_+\\]\\[';:?.,!^-]+$";

    public boolean validateLoginData(String username, String password, String publicKey) {
        if (username == null || password == null || publicKey == null)
            throw new IllegalArgumentException("De ingevoerde username, password en public key mogen niet null zijn!");
        if (username.isEmpty() || password.isEmpty() || publicKey.isEmpty())
            throw new IllegalArgumentException("De ingevoerde username, password en public key mogen niet leeg zijn!");
        if (!username.matches(REGEX_ALPHANUMERIC) || !publicKey.matches(REGEX_ALPHANUMERIC))
            throw new IllegalArgumentException("De ingevoerde username of public key mogen alleen letters" +
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
        if (publicKey.length() != 32)
            throw new IllegalArgumentException("De public key moet 32 tekens bevatten!");
        return true;
    }

    public boolean login(String username, String password, String publicKey) {
        return validateLoginData(username, password, publicKey) &&
                masterGateway.authenticateUser(username, password, publicKey).getStatus() ==
                        HanRoutingProtocol.ClientLoginResponse.Status.SUCCES;
    }
}
