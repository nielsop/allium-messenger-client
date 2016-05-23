package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.message.Message;

import java.util.List;

public interface IMessageStore {
    void addMessage(Message message);

    void findMessage(Message message);

    List<Message> getMessages(String contact);
}

