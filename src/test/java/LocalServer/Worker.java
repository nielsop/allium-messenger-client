package LocalServer;

import com.google.protobuf.InvalidProtocolBufferException;

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

    public Worker(Socket clientSocket) {
        this.clientSocket = clientSocket;
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
                    data = Arrays.copyOf(buffer, bytesRead);

                    try {
                        ClientLoginRequest clResponse = ClientLoginRequest.parseFrom(data);
                        ClientLoginResponse.Builder builder = ClientLoginResponse.newBuilder();
                        builder.setSecretHash(String.format("%s:%s", clResponse.getUsername(), clResponse.getPassword()));
                        builder.setStatus(ClientLoginResponse.Status.valueOf(ClientLogoutResponse.Status.SUCCES_VALUE));

                        System.out.println("Request processed as 'protocol' request.");
                        data = builder.build().toByteArray();
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
