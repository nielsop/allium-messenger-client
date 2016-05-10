package nl.han.asd.project.client.commonclient.utility;

/**
 * Created by Marius on 25-04-16.
 */

import static org.apache.http.conn.util.InetAddressUtils.*;

/**
 * Provides validation methods.
 */
public class Validation {

    private static final String REGEX_ALPHANUMERIC = "[a-zA-Z0-9]*";

    /**
     * Private constructor to prevent instantiation.
     */
    private Validation() {

    }

    /**
     * Validates an IP address.
     * @param address The address to check.
     * @return <tt>true</tt> if the address is valid, <tt>false</tt> otherwise.
     */
    public static boolean isValidAddress(String address) {
        return isIPv4Address(address) || isIPv6Address(address) || isIPv6StdAddress(address)
                || isIPv6HexCompressedAddress(address);
    }

    /**
     * @param port Port must be in range 1024 - 65535
     *             You can create a server on ports 1 through 65535.
     *             Port numbers less than 256 are reserved for well-known services (like HTTP on port 80) and port numbers less than 1024 require root access on UNIX systems.
     *             Specifying a port of 0 in the ServerSocket constructor results in the server listening on a random, unused port, usually >= 1024.
     *             http://www.jguru.com/faq/view.jsp?EID=17521
     */
    public static boolean isValidPort(int port) {
        if (!(port >= 0 && port <= 65535)) {
            throw new IllegalArgumentException("Port should be in range of 1024 - 65535.");
        }
        return true;
    }

    public static boolean validateLoginData(String username, String password) {
        return isValidUsername(username) && isValidPassword(password);
    }

    /**
     * Checks if the username is a valid username
     * @param username The username to check.
     * @return <tt>true</tt> if it's a valid username, <tt>false</tt> otherwise.
     */
    private static boolean isValidUsername(String username) {
        if (username == null || username.isEmpty() || !username.matches(REGEX_ALPHANUMERIC)) {
            throw new IllegalArgumentException(
                    "Ongeldige gebruikersnaam! Voer een gebruikersnaam van letters en cijfers in.");
        }
        if (username.length() < 3 || username.length() > 12) {
            throw new IllegalArgumentException(
                    "Ongeldige gebruikersnaam! Voer een gebruikersnaam van minimaal 3 en maximaal 12 tekens in.");
        }
        return true;
    }

    /**
     * Checks if the password is a valid password
     * @param password The password to check.
     * @return <tt>true</tt> if it's a valid password, <tt>false</tt> otherwise.
     */
    private static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty() || !password.matches(REGEX_ALPHANUMERIC)) {
            throw new IllegalArgumentException("Ongeldige wachtwoord! Voer een wachtwoord van letters en cijfers in.");
        }
        if (password.length() < 8 || password.length() > 16) {
            throw new IllegalArgumentException(
                    "Ongeldige wachtwoord! Voer een wachtwoord van minimaal 8 en maximaal 12 tekens in.");
        }
        return true;
    }

}
