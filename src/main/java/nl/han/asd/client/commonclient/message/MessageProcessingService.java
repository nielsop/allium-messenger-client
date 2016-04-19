package nl.han.asd.client.commonclient.message;

import nl.han.asd.client.commonclient.cryptography.IDecrypt;
import nl.han.asd.client.commonclient.store.IMessage;

public class MessageProcessingService implements IReceiveMessage {
    public IConfirmationMessageBuilder confirmationMessageBuilder;
    public IDecrypt decrypt;
    public IMessage message;

    public EncryptedMessage peelMessagePacket(Object messagePacket) {
        return EncryptedMessage.parseFrom(messagePacket);
    }
}
