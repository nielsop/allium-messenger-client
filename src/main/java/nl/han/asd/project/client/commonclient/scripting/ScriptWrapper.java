package nl.han.asd.project.client.commonclient.scripting;

import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.message.MessageBuilderService;
import nl.han.asd.project.client.commonclient.store.IContactStore;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import nl.han.asd.project.commonservices.internal.utility.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class ScriptWrapper implements IScriptWrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptWrapper.class);
    private IContactStore contactStore;
    private IMessageStore messageStore;
    private MessageBuilderService messageBuilderService = MessageBuilderService.getInstance();

    @Inject
    public ScriptWrapper(IContactStore contactStore, IMessageStore messageStore) {
        this.contactStore = Check.notNull(contactStore, "contactStore");
        this.messageStore = Check.notNull(messageStore, "messageStore");
    }

    public boolean sendMessage(String username, String message) {
        try {
            messageBuilderService.sendMessage(message, contactStore.findContact(username), contactStore.getCurrentUserAsContact());
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    public SimpleMessage[] getReceivedMessages(long dateTime) {
        Message[] receivedMessages = messageStore.getMessagesAfterDate(dateTime);
        SimpleMessage[] messages = new SimpleMessage[receivedMessages.length];
        for (int i = 0; i < messages.length; i++) {
            messages[i].message = receivedMessages[i].getText();
            messages[i].username = receivedMessages[i].getSender().getUsername();
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
