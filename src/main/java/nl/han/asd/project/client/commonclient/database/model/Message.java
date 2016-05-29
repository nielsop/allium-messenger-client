package nl.han.asd.project.client.commonclient.database.model;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.han.asd.project.client.commonclient.persistence.IPersistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

public class Message {

    private static final Logger LOGGER = LoggerFactory.getLogger(Message.class);

    private int id;
    private Contact sender;
    private Date timestamp;
    private String text;

    public Message(int id, Contact sender, Date timestamp, String message) {
        this.id = id;
        this.sender = sender;
        this.timestamp = timestamp;
        this.text = message;
    }

    public static Message fromDatabase(ResultSet result) {
        try {
            final int id = (Integer) result.getObject(1);
            final Contact sender = Contact.fromDatabase(result.getObject(2));
            final String message = (String) result.getObject(4);
            final Date timestamp = IPersistence.TIMESTAMP_FORMAT.parse(IPersistence.TIMESTAMP_FORMAT.format(result.getTimestamp(3)));
            return new Message(id, sender, timestamp, message);
        } catch (SQLException | ParseException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Contact getSender() {
        return sender;
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
        return getId() == otherMessage.getId();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getId()).append(getSender()).append(getText()).toHashCode();
    }
}
