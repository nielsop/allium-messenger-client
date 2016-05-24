package nl.han.asd.project.client.commonclient.message;

import com.google.inject.Inject;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import nl.han.asd.project.client.commonclient.node.ISendData;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageProcessingService implements IReceiveMessage,ISendMessage {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessingService.class);

    private IMessageStore messageStore;
    private IEncryptionService encryptionService;
    private ISendData sendData;

    @Inject
    public MessageProcessingService(IMessageStore messageStore, IEncryptionService encryptionService, ISendData sendData) {
        this.messageStore = messageStore;
        this.encryptionService = encryptionService;
        this.sendData = sendData;
    }

    @Override
    public void processMessage(HanRoutingProtocol.MessageWrapper encryptedMessage) {
        HanRoutingProtocol.Message message = decryptEncryptedMessage(encryptedMessage);
        messageStore.addMessage(message);
    }

    private HanRoutingProtocol.Message decryptEncryptedMessage(HanRoutingProtocol.MessageWrapper encryptedMessage) {
        byte[] messageBuffer = encryptionService.decryptData(encryptedMessage.getEncryptedData().toByteArray());
        HanRoutingProtocol.Message message = null;
        try {
            message = HanRoutingProtocol.Message.parseFrom(messageBuffer);
        } catch (InvalidProtocolBufferException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return message;
    }

    @Override
    public <T extends GeneratedMessage> void sendMessage(T message, Contact contactReciever) {

    }


    //TODO peelMessagePacket / Pakket uitpakken
}
