package nl.han.asd.project.client.commonclient.login;

import nl.han.asd.project.client.commonclient.master.IAuthentication;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.util.regex.Pattern;

/**
 * Created by DDulos on 13-Apr-16.
 */
public class LoginService implements ILogin {

    private static final String REGEX_ALPHANUMERIC = "[a-zA-Z0-9]";

    @Override
    public boolean login(String username, String password, String publicKey) {
        if (username == null || password == null || publicKey == null)
            throw new IllegalArgumentException("De ingevoerde username, password en public key mogen niet null zijn!");
        if (username.isEmpty() || password.isEmpty() || publicKey.isEmpty())
            throw new IllegalArgumentException("De ingevoerde username, password en public key mogen niet leeg zijn!");
        if (!username.matches(REGEX_ALPHANUMERIC) || !password.matches(REGEX_ALPHANUMERIC) || !publicKey.matches(REGEX_ALPHANUMERIC))
            throw new IllegalArgumentException("De ingevoerde username, password, en public key mogen alleen letters" +
                    " en cijfers bevatten!");
        if (username.length() < 3) throw new IllegalArgumentException("De username moet minstens 3 tekens bevatten!");
        if (username.length() > 12) throw new IllegalArgumentException("De username mag maximaal 12 tekens bevatten!");
        if (password.length() < 8) throw new IllegalArgumentException("Het wachtwoord moet minstens 8 tekens bevatten!");
        if (password.length() > 16) throw new IllegalArgumentException("Het wachtwoord moet minstens 16 tekens bevatten!");
        if (publicKey.length() < 32 || publicKey.length() > 32) throw new IllegalArgumentException("De public key moet 32 tekens bevatten!");
        return true;
    }
}
