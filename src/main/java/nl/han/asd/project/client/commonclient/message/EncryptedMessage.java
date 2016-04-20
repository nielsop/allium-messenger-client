package nl.han.asd.project.client.commonclient.message;

import com.google.protobuf.ByteString;

/**
 * Created by Julius on 15/04/16.
 */
public class EncryptedMessage {
    private String username;
    private String IP;
    private int port;
    private ByteString encryptedData;

    public EncryptedMessage(String username, String IP,int port,ByteString encryptedData){
        this.username = username;
        this.IP = IP;
        this.port = port;
        this.encryptedData = encryptedData;
    }

    public String getUsername() {
        return username;
    }

    public String getIP() {
        return IP;
    }

    public int getPort() {
        return port;
    }

    public ByteString getEncryptedData() {
        return encryptedData;
    }
}
