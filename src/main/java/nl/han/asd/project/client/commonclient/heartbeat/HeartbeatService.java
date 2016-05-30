package nl.han.asd.project.client.commonclient.heartbeat;

import com.google.protobuf.InvalidProtocolBufferException;
import nl.han.asd.project.client.commonclient.connection.ConnectionService;
import nl.han.asd.project.client.commonclient.connection.IConnectionService;
import nl.han.asd.project.client.commonclient.connection.UnpackedMessage;
import nl.han.asd.project.client.commonclient.master.IHeartbeat;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.net.SocketException;

public class HeartbeatService implements IConnectionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatService.class);
    private volatile boolean isRunning = true;
    private ConnectionService connectionService;
    private IHeartbeat heartbeat;

    @Inject
    public HeartbeatService(IHeartbeat heartbeat) {
        this.heartbeat = heartbeat;
    }

    public HeartbeatService(String hostName, int portNumber) throws IOException {
        connectionService = new ConnectionService(new byte[]{0x00}, this);
        connectionService.open(hostName, portNumber);
    }

    public void start() {
        HanRoutingProtocol.ClientHeartbeat.Builder builder = HanRoutingProtocol.ClientHeartbeat.newBuilder();
        builder.setUsername("test");
        builder.setSecretHash("x");

        Runnable heartbeatTask = () -> {
            while (isRunning) {
                try {
                    connectionService.write(builder);
                    Thread.sleep(25);
                } catch (InterruptedException | SocketException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        };

        Thread heartbeatThread = new Thread(heartbeatTask);
        heartbeatThread.start();
    }

    public void stop() throws IOException {
        isRunning = false;
        connectionService.close();
    }

    @Override
    public void onReceiveRead(UnpackedMessage message) {
        try {
            HanRoutingProtocol.ClientHeartbeat clientHeartbeat = HanRoutingProtocol.ClientHeartbeat.parseFrom(message.getData());
        } catch (InvalidProtocolBufferException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
