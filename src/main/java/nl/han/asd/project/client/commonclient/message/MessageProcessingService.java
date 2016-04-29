package nl.han.asd.project.client.commonclient.message;

import com.google.inject.Inject;
import nl.han.asd.project.client.commonclient.store.IMessageStore;

public class MessageProcessingService implements IReceiveMessage {
    public IMessageStore messageStore;
    //public IMessage message;

    @Inject
    public MessageProcessingService(IMessageStore messageStore) {
        this.messageStore = messageStore;
    }

    //TODO
    /*public nl.han.asd.project.client.commonclient.message.EncryptedMessage peelMessagePacket(Object messagePacket) {
        return nl.han.asd.project.client.commonclient.message.EncryptedMessage.parseFrom(messagePacket);
    }*/
}
