package nl.han.asd.project.client.commonclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * @author Julius
 * @version 1.0
 * @since 29/04/16
 */
public class Configuration {
    public static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final Logger LOGGER = LoggerFactory.getLogger(Configuration.class);
    private static final String DATABASE_ENCRYPTION_ALGORITHM = "SHA-256";

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

    public static String generateKey(String username, String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(DATABASE_ENCRYPTION_ALGORITHM);
            return String.format("%064x",
                    new java.math.BigInteger(1, messageDigest.digest((username + password).getBytes())))
                    .substring(0, 32);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return "";
    }

}
