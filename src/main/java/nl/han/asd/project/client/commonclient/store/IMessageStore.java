package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.message.Message;
import java.util.List;
import java.util.Map;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.util.List;

public interface IMessageStore {
    void addMessage(Message message);

    void saveToDatabase();

    void updateFromDatabase();

    void clear();

    Message[] getMessagesAfterDate(long dateTime);

    Map<Contact, List<Message>> getAllMessagesFromAllUsers();

    List<Message> getMessagesFromUser(final String contactName);
}

