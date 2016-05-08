package nl.han.asd.project.client.commonclient.message;

import com.google.inject.Inject;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import nl.han.asd.project.client.commonclient.cryptography.IDecrypt;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import nl.han.asd.project.protocol.HanRoutingProtocol;

public class MessageProcessingService implements IReceiveMessage {
    public IMessageStore messageStore;
    //public IMessage message;
    public IDecrypt decrypt;

    @Inject
    public MessageProcessingService(IMessageStore messageStore) {
        this.messageStore = messageStore;

    }

    @Override
    public void processMessage(HanRoutingProtocol.EncryptedMessage encryptedMessage) {
        HanRoutingProtocol.Message message = decryptEncryptedMessage(encryptedMessage);
        messageStore.addMessage(message);
    }

    private HanRoutingProtocol.Message decryptEncryptedMessage(HanRoutingProtocol.EncryptedMessage encryptedMessage){
        ByteString messageBuffer = decrypt.decryptData(encryptedMessage.getEncryptedData());
        HanRoutingProtocol.Message message = null;
        try {
            message = HanRoutingProtocol.Message.parseFrom(messageBuffer);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return message;
    }

    //TODO
    /*public nl.han.asd.project.client.commonclient.message.EncryptedMessage peelMessagePacket(Object messagePacket) {
        return nl.han.asd.project.client.commonclient.message.EncryptedMessage.parseFrom(messagePacket);
    }*/
}
