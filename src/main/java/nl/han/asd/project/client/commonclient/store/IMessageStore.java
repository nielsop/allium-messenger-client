package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.util.List;

public interface IMessageStore {
    void addMessage(HanRoutingProtocol.Message message);

    void findMessage(HanRoutingProtocol.Message message);

    List<Message> getMessages(String contact);

    void sendMessage(Message message);
}
