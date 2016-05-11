package nl.han.asd.project.client.commonclient.node;

import com.google.inject.Inject;
import nl.han.asd.project.client.commonclient.message.IReceiveMessage;

public class NodeConnectionService implements ISetConnectedNodes {
    private IReceiveMessage receiveMessage;
    private INodeConnection nodeConnection;

    @Inject
    public NodeConnectionService(IReceiveMessage receiveMessage, INodeConnection nodeConnection) {
        this.receiveMessage = receiveMessage;
        this.nodeConnection = nodeConnection;
    }
}
