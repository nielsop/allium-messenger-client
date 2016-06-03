package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.message.Message;

import java.util.List;

public interface IMessageStore extends AutoCloseable {
    /**
     * Add message to MessageStore.
     *
     * @param message Message to be added.
     */
    void addMessage(Message message);

    /**
     * Returns all messages for a certain user after a certain dateTime.
     *
     * @param dateTime unix time stamp.
     * @return an arrayList of messages.
     */
    Message[] getMessagesAfterDate(long dateTime);

    /**
     * Get all messages sent to and received from a user.
     *
     * @param username User to get messages from.
     *
     * @return List of messages.
     */
    List<Message> getMessagesFromUser(final String username);

    /**
     * Initiate the store using the provided username and password.
     *
     * @param username The user's username.
     * @param password The user's password.
     */
    void init(String username, String password);
}
