package nl.han.asd.project.client.commonclient.message;

import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.protocol.HanRoutingProtocol;

@FunctionalInterface
public interface ISendMessage {
    public void sendMessage(HanRoutingProtocol.MessageWrapper message);
}
