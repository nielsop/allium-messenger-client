package nl.han.asd.project.client.commonclient.message;

import com.google.inject.Inject;
import com.google.protobuf.InvalidProtocolBufferException;
import nl.han.asd.project.client.commonclient.node.ISendMessage;
import nl.han.asd.project.client.commonclient.store.IContactStore;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.commonservices.internal.utility.Check;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class MessageProcessingService implements IReceiveMessage, ISendMessage {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessingService.class);
    private IContactStore contactStore;
    private IMessageStore messageStore;
    private IEncryptionService encryptionService;

    @Inject
    public MessageProcessingService(IContactStore contactStore, IMessageStore messageStore, IEncryptionService encryptionService) {
        this.contactStore = Check.notNull(contactStore, "contactStore");
        this.messageStore = Check.notNull(messageStore, "messageStore");
        this.encryptionService = Check.notNull(encryptionService, "encryptionService");
    }

    @Override
    public void processMessage(HanRoutingProtocol.MessageWrapper encryptedMessage) {
        Message message = decryptEncryptedMessage(encryptedMessage);
        messageStore.addMessage(message);
    }

    private Message decryptEncryptedMessage(HanRoutingProtocol.MessageWrapper encryptedMessage) {
        byte[] messageBuffer = encryptionService.decryptData(encryptedMessage.getData().toByteArray());

        HanRoutingProtocol.Message message = null;
        try {
            message = HanRoutingProtocol.Message.parseFrom(messageBuffer);
            return new Message(contactStore.findContact(message.getSender()), contactStore.getCurrentUser().getCurrentUserAsContact(), new Date(), message.getText());
        } catch (InvalidProtocolBufferException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new Message(contactStore.findContact(message.getSender()), contactStore.getCurrentUser().getCurrentUserAsContact(), new Date(message.getTimeSent()), message.getText());
    }

    @Override
    public void sendMessage(HanRoutingProtocol.Wrapper message) {

    }


    //TODO peelMessagePacket / Pakket uitpakken
}
