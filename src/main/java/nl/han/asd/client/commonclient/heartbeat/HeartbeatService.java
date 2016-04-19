package nl.han.asd.client.commonclient.heartbeat;

import com.google.protobuf.InvalidProtocolBufferException;
import nl.han.asd.client.commonclient.connection.ConnectionService;
import nl.han.asd.client.commonclient.connection.IConnectionService;
import nl.han.asd.client.commonclient.master.IHeartbeat;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.io.IOException;

public class HeartbeatService implements IConnectionService {
    protected volatile boolean isRunning = true;

    protected ConnectionService connectionService = null;
    public IHeartbeat heartbeat;

    public HeartbeatService(String hostName, int portNumber) throws IOException {
        connectionService = new ConnectionService(this);
        connectionService.Start(hostName, portNumber);
    }

    public void Start()
    {
        HanRoutingProtocol.ClientHeartbeat.Builder builder = HanRoutingProtocol.ClientHeartbeat.newBuilder();
        builder.setUsername("test");
        builder.setSecretHash("x");

        byte[] payload = builder.build().toByteArray();

        Runnable heartbeatTask = () -> {
            while(isRunning == true)
            {
                connectionService.Write(payload);

                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread heartbeatThread = new Thread(heartbeatTask);
        heartbeatThread.start();
    }

    public void Stop() throws IOException {
        isRunning = false;
        connectionService.Close();
    }

    @Override
    public void OnReceiveRead(byte[] buffer) {
        try {
            HanRoutingProtocol.ClientHeartbeat clientHeartbeat = HanRoutingProtocol.ClientHeartbeat.parseFrom(buffer);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }
}
