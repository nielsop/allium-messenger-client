package nl.han.asd.project.client.commonclient.master;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 27-5-2016
 */
public interface ILogout {
    boolean logout(String username, String secretHash);
}
