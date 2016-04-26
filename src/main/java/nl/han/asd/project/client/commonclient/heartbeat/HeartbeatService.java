package nl.han.asd.project.client.commonclient.heartbeat;

import com.google.protobuf.InvalidProtocolBufferException;
import nl.han.asd.project.client.commonclient.connection.ConnectionService;
import nl.han.asd.project.client.commonclient.connection.IConnectionService;
import nl.han.asd.project.client.commonclient.connection.ParsedMessage;
import nl.han.asd.project.client.commonclient.master.IHeartbeat;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.io.IOException;
import java.net.SocketException;

public class HeartbeatService implements IConnectionService {
    protected volatile boolean isRunning = true;

    protected ConnectionService connectionService = null;
    public IHeartbeat heartbeat;

    public HeartbeatService(String hostName, int portNumber) throws IOException {
        connectionService = new ConnectionService("publickey", this);
        connectionService.open(hostName, portNumber);
    }

    public void Start() {
        HanRoutingProtocol.ClientHeartbeat.Builder builder = HanRoutingProtocol.ClientHeartbeat.newBuilder();
        builder.setUsername("test");
        builder.setSecretHash("x");

        Runnable heartbeatTask = () -> {
            while (isRunning == true) {
                try {
                    connectionService.write(builder);
                    Thread.sleep(25);
                } catch (InterruptedException | SocketException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread heartbeatThread = new Thread(heartbeatTask);
        heartbeatThread.start();
    }

    public void Stop() throws IOException {
        isRunning = false;
        connectionService.close();
    }
    @Override
    public void onReceiveRead(ParsedMessage message) {
        try {
            HanRoutingProtocol.ClientHeartbeat clientHeartbeat = HanRoutingProtocol.ClientHeartbeat.parseFrom(message.getData());
            // or:
            //HanRoutingProtocol.ClientHeartbeat clientHeartbeat = message.getDataMessage().getParserForType().parseFrom(message.getData());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }
}
