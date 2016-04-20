package nl.han.asd.project.client.commonclient.heartbeat;

import nl.han.asd.project.client.commonclient.connection.ConnectionService;
import nl.han.asd.project.client.commonclient.master.IHeartbeat;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.io.IOException;
import java.net.SocketException;

public class HeartbeatService  {
    protected volatile boolean isRunning = true;

    protected ConnectionService connectionService = null;
    public IHeartbeat heartbeat;

    public HeartbeatService(String hostName, int portNumber) throws IOException {
        connectionService = new ConnectionService();
        connectionService.open(hostName, portNumber);
    }

    public void start()
    {
        // Build a ClientHeartbeat message
        HanRoutingProtocol.ClientHeartbeat.Builder builder = HanRoutingProtocol.ClientHeartbeat.newBuilder();
        builder.setUsername("test");
        builder.setSecretHash("test");

        // Ready for take-off
        Runnable heartbeatTask = () -> {
            while (isRunning == true) {
                try {
                    // Write the buffer to the stream of the connected socket
                    connectionService.writeGeneric(builder);
                } catch (SocketException e) {
                    isRunning = false;
                }

                // The following needs to be discussed;
                try {
                    // Make sure the CPU has enough time to work the other threads.
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        // Houston - we've taken off!
        Thread heartbeatThread = new Thread(heartbeatTask);
        heartbeatThread.start();
    }

    public void stop() throws IOException {
        isRunning = false;
        connectionService.close();
    }
}
