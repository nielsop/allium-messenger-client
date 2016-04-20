package nl.han.asd.client.commonclient.message;

import com.google.inject.Inject;
import nl.han.asd.client.commonclient.store.IMessageStore;

public class MessageProcessingService implements IReceiveMessage {
    public IMessageStore messageStore;

    @Inject
    public MessageProcessingService(IMessageStore messageStore) {
        this.messageStore = messageStore;
    }
}
