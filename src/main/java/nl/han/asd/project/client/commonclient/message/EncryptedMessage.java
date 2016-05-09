package nl.han.asd.project.client.commonclient.message;

import com.google.protobuf.ByteString;

/**
 * Created by Marius on 25-04-16.
 */
public class EncryptedMessage {

    private final byte[] publicKey;
    private final String username;
    private final String IP;
    private final int port;
    private final ByteString encryptedData;

    public EncryptedMessage(String username, String IP, int port, byte[] publicKey ,ByteString encryptedData) {
        this.username = username;
        this.IP = IP;
        this.port = port;
        this.encryptedData = encryptedData;
        this.publicKey = publicKey;
    }

    public String getUsername() {
        return username;
    }

    public int getPort() {
        return port;
    }

    public String getIP() {
        return IP;
    }

    public ByteString getEncryptedData() {
        return encryptedData;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }
}
