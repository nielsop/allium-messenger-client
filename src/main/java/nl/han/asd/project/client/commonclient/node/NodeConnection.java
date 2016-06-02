package nl.han.asd.project.client.commonclient.node;

import nl.han.asd.project.client.commonclient.connection.IConnectionService;
import nl.han.asd.project.client.commonclient.message.IReceiveMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static nl.han.asd.project.protocol.HanRoutingProtocol.MessageWrapper;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 10-5-2016
 */
public class NodeConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeConnection.class);

    private IConnectionService connectionService;
    private IReceiveMessage receiveMessage;
    private volatile boolean isRunning = false;

    public NodeConnection(IConnectionService connectionService, IReceiveMessage receiveMessage) {
        this.connectionService = connectionService;
        this.receiveMessage = receiveMessage;
    }

    /**
     * Start the connection with the node, keep it open on another thread.
     */
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
                        LOGGER.error(e.getMessage(), e);
                    }
                }
            }
        }).start();
    }

    /**
     * Stop the connection with the node.
     */
    public void stop() {
        isRunning = false;
        connectionService.close();
    }
}
