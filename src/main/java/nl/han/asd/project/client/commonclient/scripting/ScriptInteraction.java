package nl.han.asd.project.client.commonclient.scripting;

import nl.han.asd.project.client.commonclient.message.ISendMessage;
import nl.han.asd.project.client.commonclient.message.Message;

import nl.han.asd.project.client.commonclient.store.Contact;

import nl.han.asd.project.client.commonclient.store.IContactStore;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import nl.han.asd.project.commonservices.internal.utility.Check;
import nl.han.asd.project.commonservices.scripting.internal.IScriptInteraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Date;


public class ScriptInteraction implements IScriptInteraction {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptInteraction.class);


    private IContactStore contactStore;
    private IMessageStore messageStore;
    private ISendMessage sendMessage;

    @Inject
    public ScriptInteraction(IContactStore contactStore, IMessageStore messageStore, ISendMessage sendMessage) {
        this.sendMessage = sendMessage;
        this.contactStore = Check.notNull(contactStore, "contactStore");
        this.messageStore = Check.notNull(messageStore, "messageStore");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean sendMessage(String username, String messageText) {
        try {
            Contact contact = contactStore.findContact(username);
            Message message = new Message(contactStore.getCurrentUserAsContact(), contact, new Date(), messageText);
            sendMessage.sendMessage(message, contact);
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SimpleMessage[] getReceivedMessages(Date date) {
        long dateTime = date.getTime();
        Message[] receivedMessages = messageStore.getMessagesAfterDate(dateTime);
        SimpleMessage[] messages = new SimpleMessage[receivedMessages.length];
        for (int i = 0; i < messages.length; i++) {
            messages[i].message = receivedMessages[i].getText();
            messages[i].sender = receivedMessages[i].getSender().getUsername();
        }
        return messages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printUI(IScriptInteraction.UIMessageType uiMessageType,  String s) {
        //ToDo implement

    }

    /**
     * Different types of UImessage, used by the UI to display messages in a different manner.
     */
    public enum UIMessageType {
        INFO, ERROR, DEBUG
    }
}

