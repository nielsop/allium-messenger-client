package nl.han.asd.project.client.commonclient.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import nl.han.asd.project.client.commonclient.store.IMessageStore;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import nl.han.asd.project.protocol.HanRoutingProtocol.MessageWrapper;

public class MessageProcessingService implements IReceiveMessage {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessingService.class);
    public IMessageStore messageStore;

    public IEncryptionService encryptionService;

    @Inject
    public MessageProcessingService(IMessageStore messageStore, IEncryptionService encryptionService) {
        this.messageStore = messageStore;
        this.encryptionService = encryptionService;
    }

    @Override
    public void processMessage(HanRoutingProtocol.MessageWrapper encryptedMessage) {
        HanRoutingProtocol.Message message = decryptEncryptedMessage(encryptedMessage);
        messageStore.addMessage(message);
    }

    private HanRoutingProtocol.Message decryptEncryptedMessage(MessageWrapper encryptedMessage) {
        return null;
    }

}
