package LocalServer;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.protobuf.InvalidProtocolBufferException;
import nl.han.asd.project.client.commonclient.connection.Packer;
import nl.han.asd.project.client.commonclient.connection.ParsedMessage;
import nl.han.asd.project.client.commonclient.cryptography.CryptographyService;
import nl.han.asd.project.commonservices.encryption.EncryptionModule;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

import static nl.han.asd.project.protocol.HanRoutingProtocol.*;

/**
 * Created by Jevgeni on 19-4-2016.
 */
public class Worker implements Runnable {
    protected Socket clientSocket = null;
    private Packer packer = null;

    public Worker(Socket clientSocket, Packer packer) {
        this.clientSocket = clientSocket;
        this.packer = packer;
    }


    @Override
    public void run() {
        InputStream input  = null;
        OutputStream output = null;

        try {
            input = clientSocket.getInputStream();
            output = clientSocket.getOutputStream();

            byte[] buffer = new byte[1024];
            byte[] data = null;
            try {
                int bytesRead = input.read(buffer);
                if (bytesRead > 0) {
                    try {
                        data = Arrays.copyOf(buffer, bytesRead);
                        ParsedMessage message = packer.unpack(data);

                        ClientLoginRequest request = ClientLoginRequest.parseFrom(message.getData());
                        ClientLoginResponse.Builder builder = ClientLoginResponse.newBuilder();
                        builder.setSecretHash(String.format("%s:%s", request.getUsername(), request.getPassword()));
                        builder.setStatus(ClientLoginResponse.Status.valueOf(ClientLogoutResponse.Status.SUCCES_VALUE));

                        System.out.println("Request processed as 'protocol' request.");
                        data = packer.pack(builder, "publicKey");
                    }
                    catch (InvalidProtocolBufferException e){
                        System.out.println("Request processed as 'normal' request.");
                    }

                    output.write(data);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            output.close();
            input.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
