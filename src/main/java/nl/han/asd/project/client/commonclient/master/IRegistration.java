package nl.han.asd.project.client.commonclient.master;

import nl.han.asd.project.client.commonclient.master.wrapper.RegisterResponseWrapper;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 20-4-2016
 * <p>
 * Provides methods to register a new user.
 */
public interface IRegistration {

    RegisterResponseWrapper register(String username, String password);
}
