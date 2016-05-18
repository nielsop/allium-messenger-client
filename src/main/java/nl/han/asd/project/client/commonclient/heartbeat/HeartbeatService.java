package nl.han.asd.project.client.commonclient.heartbeat;

import nl.han.asd.project.client.commonclient.connection.ConnectionService;
import nl.han.asd.project.client.commonclient.master.IHeartbeat;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketException;

public class HeartbeatService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatService.class);
    public IHeartbeat heartbeat;
    protected volatile boolean isRunning = true;
    protected ConnectionService connectionService = null;

    public HeartbeatService(String hostName, int portNumber) throws IOException {
        //TODO: Implement
        connectionService = new ConnectionService(null, new byte[] { 0x00 });
        connectionService.open(hostName, portNumber);
    }

    public void Start() {
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

    public void Stop() throws IOException {
        isRunning = false;
        connectionService.close();
    }
}
