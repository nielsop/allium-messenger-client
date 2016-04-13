package nl.han.onionmessenger.commonclient.master;

import nl.han.onionmessenger.commonclient.cryptography.IEncrypt;
import nl.han.onionmessenger.commonclient.registration.IAuthentication;

import java.io.IOException;
import java.net.Socket;

public class MasterGateway implements IGetUpdatedGraph, IGetClients, IHeartbeat, IAuthentication {
    //TODO: missing: IWebService from Master
    public IEncrypt encrypt;

    private String inaddress ;
    private int port;

    public MasterGateway(String address, int port) {
        if(address == null)
            throw new NullPointerException("Invalid adress; adress may not be null.");

        this.inaddress = address;
        this.port = port;
    }

    public String registerClient(String data)
    {
        return null;
    }

    public void sendMessage(byte[] data) throws IOException {
        if(data == null)
            throw new NullPointerException("Invalid data; data may not be null.");
        if(data.length < 1)
            throw new IllegalArgumentException("Invalid data; data length expected > 1, found; " + data.length);

    try (Socket s = new Socket(this.inaddress, this.port)) {
            s.getOutputStream().write(data);
        }
    }

}
