package nl.han.asd.project.client.commonclient.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides validation methods.
 */
public class Validation {

    /**
     * Validates the given IP4 address.
     * When the IP4 isn't valid this function will throw an error.
     * @param address Address to validate.
     */
    public void validateAddress(String address) {
        //Address may not be null
        if (address == null)
            throw new NullPointerException("Invalid adress; adress may not be null.");
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

    /**
     * @param port Port must be in range 1024 - 65535
     *             You can create a server on ports 1 through 65535.
     *             Port numbers less than 256 are reserved for well-known services (like HTTP on port 80) and port numbers less than 1024 require root access on UNIX systems.
     *             Specifying a port of 0 in the ServerSocket constructor results in the server listening on a random, unused port, usually >= 1024.
     *             http://www.jguru.com/faq/view.jsp?EID=17521
     */
    public void validatePort(int port) {
        if (!(port >= 0 && port <= 65535))
            throw new IllegalArgumentException("Port should be in range of 1024 - 65535.");
    }

}
