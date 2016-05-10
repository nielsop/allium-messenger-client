package nl.han.asd.project.client.commonclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Julius
 * @version 1.0
 * @since 29/04/16
 */
public class Configuration {

    private static final Logger logger = LoggerFactory.getLogger(Configuration.class);
    private static final int DEFAULT_PORT = 32992;
    private static final String DEFAULT_HOSTNAME = "195.169.194.234";

    public static int port = DEFAULT_PORT;
    public static String hostname = DEFAULT_HOSTNAME;

    /**
     * Private constructor to prevent instantiation.
     */
    private Configuration() {
    }

    public static int getPort() {
        try {
            FileInputStream inputStream = new FileInputStream("application.properties");
            final Properties properties = new Properties();
            properties.load(inputStream);
            return Integer.parseInt(properties.getProperty("port"));
        } catch (final IOException e) {
            logger.error(e.getMessage(), e);
        }
        return -1;
    }

    public static String getHostname() {
        try {
            FileInputStream inputStream = new FileInputStream("application.properties");
            final Properties properties = new Properties();
            properties.load(inputStream);
            return properties.getProperty("server");
        } catch (final IOException e) {
            logger.error(e.getMessage(), e);
        }
        return "";
    }
}
