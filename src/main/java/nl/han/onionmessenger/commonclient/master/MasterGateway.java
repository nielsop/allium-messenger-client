package nl.han.onionmessenger.commonclient.master;

import nl.han.onionmessenger.commonclient.cryptography.IEncrypt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MasterGateway implements IGetUpdatedGraph, IGetClients, IHeartbeat {
    //TODO: missing: IWebService from Master
    public IEncrypt encrypt;
    private PrintWriter out;
    private BufferedReader in;

    public MasterGateway(String host, int port) {
        try {
            Socket socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void login(String username, String password) {
    }

    public void write(String data) {
        out.println(data);
    }

    public String readLine() throws IOException {
        return in.readLine();
    }

}
