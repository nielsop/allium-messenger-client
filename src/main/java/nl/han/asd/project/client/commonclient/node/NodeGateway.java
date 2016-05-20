package nl.han.asd.project.client.commonclient.node;

import com.google.inject.Inject;
import com.google.protobuf.GeneratedMessage;
import nl.han.asd.project.client.commonclient.connection.ConnectionService;
import nl.han.asd.project.client.commonclient.store.Contact;

public class NodeGateway implements ISendMessage {

    private ConnectionService connectionService = null;

    @Inject
    public NodeGateway(){
    }

    @Override
    public <T extends GeneratedMessage> void sendMessage(T message, Contact contactReciever) {

    }
}
