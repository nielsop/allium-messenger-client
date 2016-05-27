package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.message.Message;

import java.util.List;
import java.util.Map;

public interface IMessageStore {
    void addMessage(Message message);

    List<Message> getMessagesFromUser(String contact);

    void saveToDatabase();

    void updateFromDatabase();

    void clear();
}

