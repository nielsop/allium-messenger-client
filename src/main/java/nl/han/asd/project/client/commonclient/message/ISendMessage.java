package nl.han.asd.project.client.commonclient.message;

import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.protocol.HanRoutingProtocol;

@FunctionalInterface
public interface ISendMessage {
    public void sendMessage(Message message, Contact contact);
}
