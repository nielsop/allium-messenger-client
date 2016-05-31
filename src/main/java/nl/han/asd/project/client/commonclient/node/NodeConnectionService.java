package nl.han.asd.project.client.commonclient.node;

import com.google.inject.Inject;
import nl.han.asd.project.client.commonclient.connection.ConnectionService;
import nl.han.asd.project.client.commonclient.connection.MessageNotSentException;
import nl.han.asd.project.client.commonclient.message.IReceiveMessage;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.IContactStore;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NodeConnectionService implements ISetConnectedNodes, ISendData {
    private IReceiveMessage receiveMessage;
    private IConnectionListener nodeConnectionService;
    private IContactStore contactStore;

    private List<NodeConnection> openConnections = new ArrayList<>();

    /**
     * Constructor of NodeConnectionService
     *
     * @param receiveMessage the receiveMessage interface
     * @param nodeConnectionService the nodeConnection interface
     */
    @Inject
    public NodeConnectionService(IReceiveMessage receiveMessage, IConnectionListener nodeConnectionService, IContactStore contactStore) {
        this.receiveMessage = receiveMessage;
        this.nodeConnectionService = nodeConnectionService;
        this.contactStore = contactStore;
    }

    @Override public void sendData(byte[] data, Contact receiver) {
    }

    /** {@inheritDoc} */
    @Override
    public void setConnectedNodes(List<String> connectedNodes) {
        for (String connectNode : connectedNodes) {
            String[] parts = connectNode.split(":");
            String hostname = parts[0];
            int port = Integer.parseInt(parts[1]);

            final ConnectionService connectionService = new ConnectionService(hostname, port);

            HanRoutingProtocol.ClientNodeConnection.Builder builder = HanRoutingProtocol.ClientNodeConnection.newBuilder();
            builder.setUsername(contactStore.getCurrentUser().getCurrentUserAsContact().getUsername());

            try {
                HanRoutingProtocol.Wrapper wrapper = connectionService.wrap(builder.build(), HanRoutingProtocol.Wrapper.Type.CLIENTNODECONNECTION);
                connectionService.write(wrapper);
            } catch (IOException | MessageNotSentException e) {
                e.printStackTrace();
            }

            NodeConnection nodeConnection = new NodeConnection(connectionService, receiveMessage);
            openConnections.add(nodeConnection);

            nodeConnection.start();
        }
    }
}
