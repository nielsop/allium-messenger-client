package nl.han.asd.project.client.commonclient.node;

import com.google.inject.Inject;
import nl.han.asd.project.client.commonclient.connection.ConnectionService;
import nl.han.asd.project.client.commonclient.connection.MessageNotSentException;
import nl.han.asd.project.client.commonclient.message.IMessageConfirmation;
import nl.han.asd.project.client.commonclient.message.IReceiveMessage;
import nl.han.asd.project.client.commonclient.store.IContactStore;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NodeConnectionService implements IConnectedNodes, ISendData {
    private IReceiveMessage receiveMessage;
    private IContactStore contactStore;
    private IMessageConfirmation messageConfirmation;

    private List<NodeConnection> openConnections = new ArrayList<>();

    /**
     * Constructor of NodeConnectionService
     *
     * @param receiveMessage the receiveMessage interface
     */
    @Inject
    public NodeConnectionService(IReceiveMessage receiveMessage, IContactStore contactStore, IMessageConfirmation messageConfirmation) {
        this.receiveMessage = receiveMessage;
        this.contactStore = contactStore;
        this.messageConfirmation = messageConfirmation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setConnectedNodes(List<String> connectedNodes) {
        for (String connectNode : connectedNodes) {
            String[] parts = connectNode.split(":");
            String hostname = parts[0];
            int port = Integer.parseInt(parts[1]);

            final ConnectionService connectionService = new ConnectionService(hostname, port);
            connectionService.closeOnIdle = false;

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

    @Override
    public void unsetConnectedNodes() {
        for (NodeConnection openConnection : openConnections) {
            openConnection.stop();
        }
        openConnections = new ArrayList<>();
    }

    @Override
    public void sendData(HanRoutingProtocol.MessageWrapper messageWrapper) {
        String hostname = messageWrapper.getIPaddress();
        int port = messageWrapper.getPort();

        try {
            HanRoutingProtocol.Wrapper wrapper = HanRoutingProtocol.Wrapper.parseFrom(messageWrapper.getData());

            ConnectionService connectionService = new ConnectionService(hostname, port);
            connectionService.write(wrapper);
        } catch (MessageNotSentException | IOException e) {
            e.printStackTrace();
        }
    }
}
