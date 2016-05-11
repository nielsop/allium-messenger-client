package nl.han.asd.project.client.commonclient.message;

import com.google.protobuf.ByteString;

/**
 * Created by Marius on 25-04-16.
 */
public class EncryptedMessage {
    private String username;
    private String ip;
    private int port;
    private ByteString encryptedData;

    public EncryptedMessage(String username, String ip, int port, ByteString encryptedData) {
        this.username = username;
        this.ip = ip;
        this.port = port;
        this.encryptedData = encryptedData;
    }

    public String getUsername() {
        return username;
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }

    public ByteString getEncryptedData() {
        return encryptedData;
    }
}
