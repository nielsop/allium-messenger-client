package nl.han.asd.project.client.commonclient.login;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 18-4-2016
 */
public interface ILogin {

    public boolean login(String username, String password, String publicKey);
}
