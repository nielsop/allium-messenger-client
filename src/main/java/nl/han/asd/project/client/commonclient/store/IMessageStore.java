package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.message.Message;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IMessageStore {
    void addMessage(Message message);

    void saveToDatabase() throws SQLException;

    void updateFromDatabase();

    void clear();

    Message[] getMessagesAfterDate(long dateTime);

    Map<Contact, List<Message>> getAllMessagesFromAllUsers();

    List<Message> getMessagesFromUser(final String contactName);
}

