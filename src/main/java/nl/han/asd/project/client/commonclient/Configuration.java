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
    private static final Logger LOGGER = LoggerFactory.getLogger(Configuration.class);

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
            LOGGER.error(e.getMessage(), e);
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
            LOGGER.error(e.getMessage(), e);
        }
        return "";
    }
}
