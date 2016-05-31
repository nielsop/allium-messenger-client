package nl.han.asd.project.client.commonclient.node;

import com.google.protobuf.ProtocolStringList;
import nl.han.asd.project.client.commonclient.message.IReceiveMessage;

import javax.inject.Inject;

public class NodeConnectionService implements ISetConnectedNodes {
    private IReceiveMessage receiveMessage;

    /**
     * Constructor of NodeConnectionService
     *
     * @param receiveMessage the receiveMessage interface
     */
    @Inject
    public NodeConnectionService(IReceiveMessage receiveMessage) {
        this.receiveMessage = receiveMessage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setConnectedNodes(ProtocolStringList connectedNodesList) {
        // TODO implement connectedNodes after login
    }
}
