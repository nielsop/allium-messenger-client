package nl.han.asd.project.client.commonclient.node;

import com.google.inject.Inject;
import nl.han.asd.project.client.commonclient.message.IReceiveMessage;

public class NodeConnectionService implements ISetConnectedNodes, ISendData {
    private IReceiveMessage receiveMessage;
    private IConnectionListner nodeConnection;

    @Inject
    public NodeConnectionService(IReceiveMessage receiveMessage, IConnectionListner nodeConnection) {
        this.receiveMessage = receiveMessage;
        this.nodeConnection = nodeConnection;
    }
}
