package nl.han.asd.project.client.commonclient.heartbeat;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.protobuf.InvalidProtocolBufferException;
import nl.han.asd.project.client.commonclient.connection.ConnectionModule;
import nl.han.asd.project.client.commonclient.connection.ConnectionService;
import nl.han.asd.project.client.commonclient.connection.IConnectionServiceFactory;
import nl.han.asd.project.client.commonclient.connection.UnpackedMessage;
import nl.han.asd.project.client.commonclient.cryptography.CryptographyService;
import nl.han.asd.project.client.commonclient.master.IHeartbeat;
import nl.han.asd.project.commonservices.encryption.EncryptionModule;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
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

    public HeartbeatService(final IConnectionServiceFactory connectionServiceFactory,
            final String hostName, final int portNumber) throws IOException {
        connectionService = connectionServiceFactory.create(new byte[] { 0x00 });
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
