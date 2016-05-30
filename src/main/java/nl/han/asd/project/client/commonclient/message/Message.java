package nl.han.asd.project.client.commonclient.message;

import nl.han.asd.project.client.commonclient.Configuration;
import nl.han.asd.project.client.commonclient.store.Contact;
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
    private Date timestamp;
    private int id;

    public Message(Contact sender, Date timestamp, String text) {
        this(-1, sender, timestamp, text);
    }

    public Message(int id, Contact sender, Date timestamp, String text) {
        this.databaseId = id;
        this.sender = sender;
        this.timestamp = timestamp;
        this.text = text;
    }

    public static Message fromDatabase(ResultSet result) {
        try {
            final int id = (Integer) result.getObject(1);
            final Contact sender = Contact.fromDatabase((String) result.getObject(2));
            final String message = (String) result.getObject(4);
            final Date timestamp = Configuration.TIMESTAMP_FORMAT.parse(Configuration.TIMESTAMP_FORMAT.format(result.getTimestamp(3)));
            return new Message(id, sender, timestamp, message);
        } catch (SQLException | ParseException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    public Contact getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public String getMessageId() {
        return messageId;
    }

    public Date getMessageTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Message[sender=").append(sender.getUsername()).append(", timestamp=").append(timestamp).append(", text=").append(text).append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object anotherObject) {
        if (anotherObject == null || !(anotherObject instanceof Message)) {
            return false;
        }
        final Message otherMessage = (Message) anotherObject;
        return getText().equalsIgnoreCase(otherMessage.getText()) && getSender().equals(otherMessage.getSender());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getMessageId()).append(getSender()).append(getText()).toHashCode();
    }

    public int getDatabaseId() {
        return databaseId;
    }

public Date getTimestamp() {
        return timestamp;
    }

    public int getId() {
        return id;
    }
}
