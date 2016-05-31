package nl.han.asd.project.client.commonclient.message;

import com.google.protobuf.ByteString;

public class EncryptedMessage {

    private final byte[] publicKey;
    private String username;
    private String ip;
    private int port;
    private ByteString encryptedData;

    public EncryptedMessage(String username, String ip, int port, byte[] publicKey, ByteString encryptedData) {
        this.username = username;
        this.ip = ip;
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

    public String getIp() {
        return ip;
    }

    public ByteString getEncryptedData() {
        return encryptedData;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }
}
