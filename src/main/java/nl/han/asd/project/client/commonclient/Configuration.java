package nl.han.asd.project.client.commonclient;

/**
 * @author Julius
 * @version 1.0
 * @since 29/04/16
 */
public class Configuration {
    public static final int PORT = 32992;
    public static String hostname = "195.169.194.234";

    /**
     * Private constructor to prevent instantiation.
     */
    private Configuration() {

    }

    /**
     * Sets the hostname.
     * @param newHostname The new hostname.
     */
    public static void setHostname(final String newHostname) {
        hostname = newHostname;
    }
}
