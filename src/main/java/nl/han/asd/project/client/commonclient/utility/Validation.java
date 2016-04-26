package nl.han.asd.project.client.commonclient.utility;

/**
 * Created by Marius on 25-04-16.
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides validation methods.
 */
public class Validation {

    private static final String REGEX_ALPHANUMERIC = "[a-zA-Z0-9]";
    private static final String REGEX_ALPHANUMERICSPECIAL = "^(?=(?:\\D*?\\d){8,32}(?!.*?\\d))[a-zA-Z0-9@\\#$%&*()_+\\]\\[';:?.,!^-]+$";

    /**
     * Validates the given IP4 address.
     * When the IP4 isn't valid this function will throw an error.
     *
     * @param address Address to validate.
     */
    public static void validateAddress(String address) {
        //Address may not be null
        if (address == null)
            throw new NullPointerException("Invalid adress; adress may not be null.");
        if (!address.equalsIgnoreCase("localhost")) {
            //IP regular expression
            String ipPattern = "^([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})$";
            //Create pattern object
            int[] addressAsArray = new int[4];
            Pattern r = Pattern.compile(ipPattern);
            //Create matcher object
            Matcher m = r.matcher(address);
            //Check if match is found
            if (m.find()) {
                for (int i = 1; i < 5; i++) {
                    //Parse every group to int
                    int ipGroup = Integer.parseInt(m.group(i));
                    //Check if first value is not 0.
                    if (i == 1 && ipGroup == 0)
                        throw new IllegalArgumentException("First value may not be 0.");
                    //Check if at least one group is greater than 254
                    if (ipGroup > 254)
                        throw new IllegalArgumentException("One of the IP-values is greater than 254.");
                        //If all values are correct, put the values in an array => [xxx, xxx, xxx, xxx]
                    else
                        addressAsArray[i - 1] = ipGroup;
                }
            }
            //No match found
            else {
                throw new IllegalArgumentException("IP format is not valid. Must be xxx.xxx.xxx.xxx");
            }
        }
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
            throw new IllegalArgumentException("Port should be in range of 1024 - 65535.");
    }

    public static boolean validateLoginData(String username, String password) {
        if (username == null || password == null)
            throw new IllegalArgumentException("De ingevoerde username en password mogen niet null zijn!");
        if (username.isEmpty() || password.isEmpty())
            throw new IllegalArgumentException("De ingevoerde username en password mogen niet leeg zijn!");
        if (!username.matches(REGEX_ALPHANUMERIC))
            throw new IllegalArgumentException("De ingevoerde username mag alleen letters" +
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
        return true;
    }

}
