package nl.han.asd.project.client.commonclient.message;

import com.google.inject.Inject;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import nl.han.asd.project.client.commonclient.store.IContactStore;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageProcessingService implements IReceiveMessage {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessingService.class);
    public IMessageStore messageStore;
    public IContactStore contactStore;
    public IEncryptionService encryptionService;

    @Inject
    public MessageProcessingService(IMessageStore messageStore, IContactStore contactStore, IEncryptionService encryptionService) {
        this.messageStore = messageStore;
        this.contactStore = contactStore;
        this.encryptionService = encryptionService;
    }

    @Override
    public void processMessage(HanRoutingProtocol.MessageWrapper encryptedMessage) {
        Message message = decryptEncryptedMessage(encryptedMessage);
        messageStore.addMessage(message);
    }

    private Message decryptEncryptedMessage(HanRoutingProtocol.MessageWrapper encryptedMessage) {
        ByteString messageBuffer = ByteString.copyFrom(encryptionService.decryptData(encryptedMessage.getData().toByteArray()));
        HanRoutingProtocol.Message message = null;
        try {
            message = HanRoutingProtocol.Message.parseFrom(messageBuffer);
        } catch (InvalidProtocolBufferException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new Message(message.getText(), contactStore.findContact(message.getSender()), contactStore.getCurrentUser(), System.currentTimeMillis());
    }

    //TODO peelMessagePacket / Pakket uitpakken
}
