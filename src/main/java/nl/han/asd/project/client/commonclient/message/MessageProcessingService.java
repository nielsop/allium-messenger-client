package nl.han.asd.project.client.commonclient.message;

import com.google.inject.Inject;
import nl.han.asd.project.client.commonclient.cryptography.IDecrypt;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import nl.han.asd.project.protocol.HanRoutingProtocol;

public class MessageProcessingService implements IReceiveMessage {
    public IMessageStore messageStore;
    public IDecrypt decrypt;

    @Inject
    public MessageProcessingService(IMessageStore messageStore, IDecrypt decrypt) {
        this.messageStore = messageStore;
        this.decrypt = decrypt;
    }

    @Override
    public void processMessage(HanRoutingProtocol.Message message) {

        messageStore.addMessage(message);
    }

    //TODO
    /*public nl.han.asd.project.client.commonclient.message.EncryptedMessage peelMessagePacket(Object messagePacket) {
        return nl.han.asd.project.client.commonclient.message.EncryptedMessage.parseFrom(messagePacket);
    }*/
}
