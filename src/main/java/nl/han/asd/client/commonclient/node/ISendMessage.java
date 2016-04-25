package nl.han.asd.client.commonclient.node;

import nl.han.asd.client.commonclient.message.EncryptedMessage;

public interface ISendMessage {
    void sendMessage(EncryptedMessage message);
}
