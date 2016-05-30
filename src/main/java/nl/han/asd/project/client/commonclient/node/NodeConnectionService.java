package nl.han.asd.project.client.commonclient.node;

import com.google.protobuf.ProtocolStringList;
import nl.han.asd.project.client.commonclient.message.IReceiveMessage;

import javax.inject.Inject;

public class NodeConnectionService implements ISetConnectedNodes, ISendData {
    private IReceiveMessage receiveMessage;
    private IConnectionListener nodeConnection;

    /**
     * Constructor of NodeConnectionService
     *
     * @param receiveMessage the receiveMessage interface
     * @param nodeConnection the nodeConnection interface
     */
    @Inject
    public NodeConnectionService(IReceiveMessage receiveMessage, IConnectionListener nodeConnection) {
        this.receiveMessage = receiveMessage;
        this.nodeConnection = nodeConnection;
    }

    /** {@inheritDoc} */
    @Override
    public void setConnectedNodes(ProtocolStringList connectedNodesList) {
        // TODO implement connectedNodes after login
    }
}
