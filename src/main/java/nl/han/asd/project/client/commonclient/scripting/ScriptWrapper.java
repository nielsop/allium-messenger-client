package nl.han.asd.project.client.commonclient.scripting;

import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.message.MessageBuilderService;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.ContactStore;
import nl.han.asd.project.client.commonclient.store.MessageStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Marius
 * @version 1.0
 * @since 19-05-16
 */
public class ScriptWrapper implements IScriptWrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptWrapper.class);
    private ContactStore contactStore = ContactStore.getInstance();
    private MessageStore messageStore = MessageStore.getInstance();
    private MessageBuilderService messageBuilderService = MessageBuilderService.getInstance();

    public boolean sendMessage(String username, String message) {
        try {
            Contact receiver = contactStore.findContact(username);
            messageBuilderService.sendMessage(message, receiver, contactStore.getCurrentUser());
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        }
    }

    public SimpleMessage[] getReceivedMessages(String date) {
        List<Message> receivedMessages = messageStore.getMessagesAfterDate(date);
        SimpleMessage[] messages = new SimpleMessage[receivedMessages.size()];
        for (int i = 0; i < messages.length; i++) {
            messages[i].message = receivedMessages.get(i).getText();
            messages[i].username = receivedMessages.get(i).getSender().getUsername();
        }
        return messages;
    }

    @Override
    public void printUI(IScriptWrapper.UIMessageType type, String message) {

    }

    public enum UIMessageType {
        INFO, ERROR, DEBUG
    }
}
