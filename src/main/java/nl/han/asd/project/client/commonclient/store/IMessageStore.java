package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.util.List;

public interface IMessageStore {
    void messageReceived(String confirmationId);
    void addMessage(Message message);
    void findMessage(Message message);

    HanRoutingProtocol.Message findMessageByID(String identifier);
    List<Message> getMessagesFromUser(String contact);
}

