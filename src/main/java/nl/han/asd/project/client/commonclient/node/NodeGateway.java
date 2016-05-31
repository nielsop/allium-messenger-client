package nl.han.asd.project.client.commonclient.node;

import nl.han.asd.project.client.commonclient.message.ISendMessage;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.protocol.HanRoutingProtocol;

public class NodeGateway implements ISendMessage {

    @Override
    public void sendMessage(HanRoutingProtocol.MessageWrapper messageWrapper,
            Contact contactReceiver) {
        throw new UnsupportedOperationException();
    }
}
