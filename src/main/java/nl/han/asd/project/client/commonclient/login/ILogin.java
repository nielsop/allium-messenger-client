package nl.han.asd.project.client.commonclient.login;

import java.io.IOException;

import nl.han.asd.project.client.commonclient.connection.MessageNotSendException;
import nl.han.asd.project.client.commonclient.store.Contact;

/**
 * Define the login realated methods.
 *
 * @version 1.0
 */
public interface ILogin {

    /**
     * Check the provided user information and
     * send these details to the master server for
     * authentication.
     *
     * @param username identifying name of the user
     * @param password password
     *
     * @return instance holding all user details including
     *          the secret hash returned from the master application
     *
     * @throws IllegalArgumentException if username and/or password
     *          is null
     * @throws IllegalUsernameException if the username is
     *          invalid according to the format specified in
     *          {@link UserCheck#checkUsername(String)}
     * @throws IllegalPasswordException if the password
     *          is invalid according to the format specified
     *          in {@link UserCheck#checkPassword(String)}
     * @throws InvalidCredentialsException if the username - password
     *          combination is invalid
     * @throws IOException
     * @throws MessageNotSendException
     */
    Contact login(String username, String password)
            throws InvalidCredentialsException, IOException, MessageNotSendException;
}
