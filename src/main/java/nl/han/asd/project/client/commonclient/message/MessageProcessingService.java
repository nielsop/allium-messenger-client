package nl.han.asd.project.client.commonclient.message;

import nl.han.asd.project.client.commonclient.cryptography.IDecrypt;
import nl.han.asd.project.client.commonclient.store.IMessage;

public class MessageProcessingService implements IReceiveMessage {
    public IConfirmationMessageBuilder confirmationMessageBuilder;
    public IDecrypt decrypt;
    public IMessage message;
}
