package nl.han.asd.project.client.commonclient.node;

import nl.han.asd.project.client.commonclient.message.EncryptedMessage;

@FunctionalInterface
public interface ISendMessage {
    void sendMessage(EncryptedMessage message);
}
