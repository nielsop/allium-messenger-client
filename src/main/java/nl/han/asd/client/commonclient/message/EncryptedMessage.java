package nl.han.asd.client.commonclient.message;

/**
 * Created by BILLPOORTS on 13-4-2016.
 */
public class EncryptedMessage {
    private String ipaddress = "128.0.0.1";
    private int port = 81;
    private String username = "ikzelf";
    private byte[] encryptedData = new byte[] { 0x10, 0x11, 0x12 };

    public static EncryptedMessage parseFrom(Object input)
    {
        return new EncryptedMessage();
    }


    public String getIpaddress() {
        return ipaddress;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public byte[] getEncryptedData() {
        return encryptedData;
    }
}
