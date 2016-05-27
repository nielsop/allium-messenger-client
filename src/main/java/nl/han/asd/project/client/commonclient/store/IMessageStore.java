package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.message.Message;

import java.util.List;
import java.util.Map;

public interface IMessageStore {
    void addMessage(Message message);

    void findMessage(Message message);

    Map<Contact, List<Message>> getAllMessagesFromAllUsers();

    List<Message> getMessagesFromUser(String contact);

}

