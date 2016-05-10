package nl.han.asd.project.client.commonclient.utility;

/**
 * Created by Marius on 25-04-16.
 */

import org.apache.http.conn.util.InetAddressUtils;

import static org.apache.http.conn.util.InetAddressUtils.*;

/**
 * Provides validation methods.
 */
public class Validation {

    private static final String REGEX_ALPHANUMERIC = "[a-zA-Z0-9]*";

//    private Validation(){
//
//    }

    /**
     *
     * @param address
     * @return
     */
    public static boolean validateAddress(String address) {

        if(isIPv4Address(address) || isIPv6Address(address) || isIPv6StdAddress(address) || isIPv6HexCompressedAddress(address)){
            return true;
        }
        else return false;
    }

    /**
     * @param port Port must be in range 1024 - 65535
     *             You can create a server on ports 1 through 65535.
     *             Port numbers less than 256 are reserved for well-known services (like HTTP on port 80) and port numbers less than 1024 require root access on UNIX systems.
     *             Specifying a port of 0 in the ServerSocket constructor results in the server listening on a random, unused port, usually >= 1024.
     *             http://www.jguru.com/faq/view.jsp?EID=17521
     */
    public static void validatePort(int port) {
        if (!(port >= 0 && port <= 65535))
            throw new IllegalArgumentException(
                    "Port should be in range of 1024 - 65535.");
    }

    public static boolean validateLoginData(String username, String password) {
        if (username == null || password == null)
            throw new IllegalArgumentException(
                    "De ingevoerde username en password mogen niet null zijn!");
        if (username.isEmpty() || password.isEmpty())
            throw new IllegalArgumentException(
                    "De ingevoerde username en password mogen niet leeg zijn!");
        if (!username.matches(REGEX_ALPHANUMERIC))
            throw new IllegalArgumentException(
                    "De ingevoerde username mag alleen letters" +
                            " en cijfers bevatten!" + username);
        if (!password.matches(REGEX_ALPHANUMERIC)) {
            throw new IllegalArgumentException(
                    "De ingevoerde password mag alleen letters"
                            + " en cijfers bevatten!");
        }
        if (username.length() < 3)
            throw new IllegalArgumentException(
                    "De username moet minstens 3 tekens bevatten!");
        if (username.length() > 12)
            throw new IllegalArgumentException(
                    "De username mag maximaal 12 tekens bevatten!");
        if (password.length() < 8)
            throw new IllegalArgumentException(
                    "Het wachtwoord moet minstens 8 tekens bevatten!");
        if (password.length() > 16)
            throw new IllegalArgumentException(
                    "Het wachtwoord mag maximaal 16 tekens bevatten!");
        return true;
    }

}
