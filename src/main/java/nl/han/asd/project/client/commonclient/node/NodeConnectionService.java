package nl.han.asd.project.client.commonclient.node;

import com.google.inject.Inject;
import com.google.protobuf.ProtocolStringList;
import nl.han.asd.project.client.commonclient.message.IReceiveMessage;

public class NodeConnectionService implements ISetConnectedNodes, ISendData {
    private IReceiveMessage receiveMessage;
    private IConnectionListener nodeConnection;

    @Inject
    public NodeConnectionService(IReceiveMessage receiveMessage, IConnectionListener nodeConnection) {
        this.receiveMessage = receiveMessage;
        this.nodeConnection = nodeConnection;
    }

    @Override
    public void setConnectedNodes(ProtocolStringList connectedNodesList) {
    }
}
