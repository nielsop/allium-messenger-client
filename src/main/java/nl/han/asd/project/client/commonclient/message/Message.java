package nl.han.asd.project.client.commonclient.message;

import nl.han.asd.project.client.commonclient.persistence.IPersistence;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

public class Message {
    private static final Logger LOGGER = LoggerFactory.getLogger(Message.class);
    private String messageId;
    private int databaseId;

    private String text;
    private Contact sender;
    private Contact receiver;
    private Date timestamp;

    /**
     * Constructs a new Message object without a database ID.
     *
     * @param sender The message's sender.
     * @param receiver The message recipient.
     * @param timestamp Timestamp of when the message was sent.
     * @param text The actual message.
     */
    public Message(Contact sender, Contact receiver, Date timestamp, String text) {
        this(-1, sender, receiver, timestamp, text);
    }

    /**
     * Constructs a new Message object with a database ID.
     *
     * @param id The database id.
     * @param sender The message's sender.
     * @param receiver The message recipient.
     * @param timestamp Timestamp of when the message was sent.
     * @param text The actual message.
     */
    public Message(int id, Contact sender, Contact receiver, Date timestamp, String text) {
        databaseId = id;
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = timestamp;
        this.text = text;
    }

    /**
     * Creates a new Message object from a database result.
     *
     * @param result The resultset (result of a database query) to parse a Message from.
     * @return A new Message object from a database result.
     */
    public static Message fromDatabase(ResultSet result) {
        try {
            final int id = (Integer) result.getObject(1);
            final Contact sender = Contact.fromDatabase((String) result.getObject(2));
            final Contact receiver = Contact.fromDatabase((String) result.getObject(3));
            final Date timestamp = IPersistence.TIMESTAMP_FORMAT.parse(IPersistence.TIMESTAMP_FORMAT.format(result.getTimestamp(4)));
            final String message = (String) result.getObject(5);
            return new Message(id, sender, receiver, timestamp, message);
        } catch (SQLException | ParseException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    public static Message fromProtocolMessage(HanRoutingProtocol.Message protocolMessage, Contact receiver)
    {
        final int id = Integer.parseInt(protocolMessage.getId());
        final Contact sender = Contact.fromDatabase(protocolMessage.getSender());
        final String text = protocolMessage.getText();
        final Date timestamp = new Date(protocolMessage.getTimeSent());
        return new Message(id, receiver, sender, timestamp, text);
    }


    /**
     * Returns the message's sender.
     * @return The message's sender.
     */
    public Contact getSender() {
        return sender;
    }

    /**
     * Returns the actual message.
     * @return The actual message.
     */
    public String getText() {
        return text;
    }

    /**
     * Returns the message ID.
     * @return The message ID.
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Returns the message timestamp.
     * @return A timestamp of when the message was sent.
     */
    public Date getMessageTimestamp() {
        return timestamp;
    }

    /**
     * Returns the message's recipient.
     * @return The message's recipient.
     */
    public Contact getReceiver() {
        return receiver;
    }

    @Override
    public String toString() {
        return "Message[sender=" + sender.getUsername() + ", receiver=" + receiver.getUsername() + ", timestamp=" + timestamp + ", text=" + text + "]";
    }

    @Override
    public boolean equals(Object anotherObject) {
        if (anotherObject == null || !(anotherObject instanceof Message)) {
            return false;
        }
        final Message otherMessage = (Message) anotherObject;
        return getText().equalsIgnoreCase(otherMessage.getText()) && getSender().equals(otherMessage.getSender()) && getReceiver().equals(otherMessage.getReceiver());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getMessageId()).append(getSender()).append(getText()).append(getReceiver()).toHashCode();
    }

    /**
     * Returns the database ID for this Message object.
     * @return The database ID for this Message object.
     */
    public int getDatabaseId() {
        return databaseId;
    }

}
