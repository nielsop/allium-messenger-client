package nl.han.asd.project.client.commonclient.node;

import nl.han.asd.project.client.commonclient.message.EncryptedMessage;
import nl.han.asd.project.client.commonclient.utility.RequestWrapper;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.io.IOException;
import java.net.Socket;

public class NodeGateway implements ISendMessage {

    private INodeGateway nodeGateway;

    //TODO: Fix Dependency Injection
    /*@Inject
    public NodeGateway(INodeGateway nodeGateway) {
        this.nodeGateway = nodeGateway;
    }*/

    @Override
    public void sendMessage(EncryptedMessage message) {
    }
}
