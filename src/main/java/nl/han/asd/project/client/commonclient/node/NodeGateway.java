package nl.han.asd.project.client.commonclient.node;

import nl.han.asd.project.client.commonclient.message.EncryptedMessage;
import nl.han.asd.project.client.commonclient.utility.RequestWrapper;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import javax.inject.Inject;
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
        //build connection based on message ip & post

        //build protocol buffer based on message data

        //send message with build connection.
        Socket socket;

        try {
            socket = new Socket(message.getIP(), message.getPort());

            HanRoutingProtocol.EncryptedMessage.Builder request = HanRoutingProtocol.EncryptedMessage.newBuilder();

            request.setIPaddress(message.getIP());
            request.setPort(message.getPort());
            request.setEncryptedData(message.getEncryptedData());

            final RequestWrapper req = new RequestWrapper(request.build(), socket);
            req.writeToSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
