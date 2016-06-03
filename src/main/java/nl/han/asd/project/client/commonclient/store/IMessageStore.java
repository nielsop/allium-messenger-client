package nl.han.asd.project.client.commonclient.store;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import nl.han.asd.project.client.commonclient.message.Message;

public interface IMessageStore extends AutoCloseable {
    void addMessage(Message message);

    void saveToDatabase() throws SQLException;

    void updateFromDatabase();

    void clear();

    /**
     * Returns all messages for a certain user after a certain dateTime.
     *
     * @param dateTime unix time stamp.
     * @return an arrayList of messages.
     */
    Message[] getMessagesAfterDate(long dateTime);

    Map<Contact, List<Message>> getAllMessagesFromAllUsers();

    List<Message> getMessagesFromUser(final String contactName);

    /**
     * Initiate the store using the provided username and password.
     *
     * @param username The user's username.
     * @param password The user's password.
     */
    void init(String username, String password);
}
