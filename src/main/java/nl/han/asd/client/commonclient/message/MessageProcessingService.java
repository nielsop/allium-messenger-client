package nl.han.asd.client.commonclient.message;

import com.google.inject.Inject;
import nl.han.asd.client.commonclient.cryptography.IDecrypt;
import nl.han.asd.client.commonclient.store.IMessageStore;

public class MessageProcessingService implements IReceiveMessage {
    public IMessageStore messageStore;
    public IDecrypt decrypt;
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
