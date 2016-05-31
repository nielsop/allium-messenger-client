package nl.han.asd.project.client.commonclient.node;

import nl.han.asd.project.client.commonclient.connection.IConnectionService;
import nl.han.asd.project.client.commonclient.message.IReceiveMessage;

import java.io.IOException;

import static nl.han.asd.project.protocol.HanRoutingProtocol.MessageWrapper;

/**
 *
 *
 * @author Niels Bokmans
 * @version 1.0
 * @since 10-5-2016
 */
public class NodeConnection implements IConnectionListener {

    private IConnectionService connectionService;
    private IReceiveMessage receiveMessage;
    private volatile boolean isRunning = false;

    public NodeConnection(IConnectionService connectionService, IReceiveMessage receiveMessage) {
        this.connectionService = connectionService;
        this.receiveMessage = receiveMessage;
    }

    public void start() {
        isRunning = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        MessageWrapper message = (MessageWrapper) connectionService.read();
                        receiveMessage.processIncomingMessage(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void stop() {
        isRunning = false;
    }
}
