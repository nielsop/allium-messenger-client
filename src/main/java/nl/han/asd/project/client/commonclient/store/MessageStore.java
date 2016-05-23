package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.persistence.IPersistence;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class MessageStore implements IMessageStore, IMessageObserver {
    private static MessageStore instance = null;
    public IPersistence persistence;
    public List<Message> messages;
    private Contact currentUser = new Contact("Kenny", "publicKey", true);

    @Inject
    public MessageStore(IPersistence persistence) {
        this.persistence = persistence;
        instance = this;
    }

    private MessageStore() {

    }

    public static MessageStore getInstance() {
        if (instance == null) {
            instance = new MessageStore();
        }
        return instance;
    }

    @Override
    public void addMessage(HanRoutingProtocol.Message message) {
        //TODO: implement!
    }

    @Override
    public void findMessage(HanRoutingProtocol.Message message) {
        //TODO: implement!
    }

    /**
     * Returns all messages for a certain user after a certain dateTime.
     * @param dateTime unix time stamp.
     * @return an arrayList of messages.
     */
    @Override
    public List<Message> getMessagesAfterDate(long dateTime) {
        List<Message> messageListForCurrentUserAfterDate = new ArrayList<>();
        for (Message message : messages) {
            if (!message.getSender().getUsername().equals(currentUser.getUsername()) && message.getMessageDateTime() >= dateTime) {
                messageListForCurrentUserAfterDate.add(message);
            }
        }
        return messageListForCurrentUserAfterDate;
    }
}
