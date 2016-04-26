package server;

import com.google.protobuf.ByteString;
import nl.han.asd.client.commonclient.connection.ConnectionService;
import nl.han.asd.client.commonclient.connection.IConnectionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.io.IOException;

import static nl.han.asd.project.protocol.HanRoutingProtocol.ClientLoginResponse;

/**
 * Created by Jevgeni on 19-4-2016.
 */
public class MainClient implements IConnectionService {

    private ConnectionService connectionService = null;

    public MainClient() throws IOException, InterruptedException {
        run();
    }

    public void run() throws IOException, InterruptedException {
        connectionService = new ConnectionService(this);
        connectionService.open("10.182.5.214", 4444);

        HanRoutingProtocol.EncryptedMessage.Builder encryptedMessageBuilder = HanRoutingProtocol.EncryptedMessage.newBuilder();
        encryptedMessageBuilder.setIPaddress("127.0.0.1");
        encryptedMessageBuilder.setPort(5555);
        encryptedMessageBuilder.setUsername("Jev");

        encryptedMessageBuilder.setEncryptedData(ByteString.copyFrom(new byte[]{0x48, 0x6F, 0x69, 0x2C, 0x20, 0x69, 0x6B, 0x20, 0x62, 0x65, 0x6E, 0x20, 0x4A, 0x65, 0x76, 0x67, 0x65, 0x6E, 0x69}, 0, 19));
        //encryptedMessageBuilder.setEncryptedData(ByteString.copyFrom(new byte[]{0x4A, 0x75, 0x6C, 0x6C, 0x69, 0x65, 0x20, 0x68, 0x65, 0x62, 0x62, 0x65, 0x6E, 0x20, 0x61, 0x6C, 0x6C, 0x65, 0x6D, 0x61, 0x61, 0x6C, 0x20, 0x65, 0x65, 0x6E, 0x20, 0x6F, 0x6E, 0x76, 0x6F, 0x6C, 0x64, 0x6F, 0x65, 0x6E, 0x64, 0x65, 0x2E, }, 0, 39));

        connectionService.writeGeneric(encryptedMessageBuilder);

        byte[] payload = connectionService.read();
        System.out.println(new String(payload, "UTF-8"));
        connectionService.close();
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
