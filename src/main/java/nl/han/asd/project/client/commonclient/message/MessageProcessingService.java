package nl.han.asd.project.client.commonclient.message;

import com.google.inject.Inject;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import nl.han.asd.project.client.commonclient.connection.UnpackedMessage;
import nl.han.asd.project.client.commonclient.cryptography.IDecrypt;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageProcessingService implements IReceiveMessage {

    private IMessageStore messageStore;
    private IDecrypt decrypt;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessingService.class);

    @Inject public MessageProcessingService(IMessageStore messageStore, IDecrypt decrypt) {
        this.messageStore = messageStore;
        this.decrypt = decrypt;
    }

    @Override public void processMessage(UnpackedMessage unpackedMessage) {
        try {
            if (unpackedMessage.matchDataMessage(HanRoutingProtocol.MessageWrapper.class)) {
                HanRoutingProtocol.MessageWrapper wrapper = HanRoutingProtocol.MessageWrapper
                        .parseFrom(unpackedMessage.getData());

                HanRoutingProtocol.Message message = decryptEncryptedMessage(
                        wrapper);

                messageStore.addMessage(message, wrapper.getUsername());
            } else {
                throw new InvalidProtocolBufferException("No valid ProtocolBuffer type.");
            }
        } catch (InvalidProtocolBufferException e) {
            LOGGER.error("Error unpacking received message.", e);
        } finally {
            // Wie moet de confirmation message verzenden?
            // Ook als het verwerken van het bericht mislukt is?
            //
            // SAD -> IDecrypt -> PersistenceService ???
        }
    }

    private HanRoutingProtocol.Message decryptEncryptedMessage(
            HanRoutingProtocol.MessageWrapper encryptedMessage)
            throws InvalidProtocolBufferException {
        ByteString messageBuffer = decrypt.decryptData(encryptedMessage.getEncryptedData());
        return HanRoutingProtocol.Message.parseFrom(messageBuffer); //wrapper
    }
}
