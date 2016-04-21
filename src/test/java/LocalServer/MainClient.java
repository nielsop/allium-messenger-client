package LocalServer;

import nl.han.asd.project.client.commonclient.connection.ConnectionService;
import nl.han.asd.project.client.commonclient.connection.IConnectionService;

import java.io.IOException;

import static nl.han.asd.project.protocol.HanRoutingProtocol.*;

/**
 * Created by Jevgeni on 19-4-2016.
 */
public class MainClient implements IConnectionService {

    public MainClient() throws IOException, InterruptedException {
        run();
    }

    private ConnectionService connectionService = null;

    public void run() throws IOException, InterruptedException {
        connectionService = new ConnectionService(this);
        connectionService.open("10.182.5.214", 4444);

        ClientLoginRequest.Builder requestBuilder = ClientLoginRequest.newBuilder();
        requestBuilder.setUsername("test");
        requestBuilder.setPassword("test");
        requestBuilder.setPublicKey("test");

        //byte[] buffer = requestBuilder.build.toByteArray();
        //connectionService.Write(buffer);
        connectionService.writeGeneric(requestBuilder);

        //byte[] buffer = connectionService.ReadGeneric(HanRoutingProtocol.ClientLoginResponse.class);
        ClientLoginResponse clr = connectionService.readGeneric(ClientLoginResponse.class);
        System.out.println(clr.getSecretHash());
    }

    @Override
    public void onReceiveRead(byte[] buffer) {
        try {
            ClientLoginResponse response = ClientLoginResponse.parseFrom(buffer);
            System.out.println(response.getSecretHash());
            connectionService.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
