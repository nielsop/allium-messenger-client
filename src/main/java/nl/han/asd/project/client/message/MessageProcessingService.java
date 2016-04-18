package nl.han.asd.project.client.message;

import nl.han.asd.project.client.cryptography.IDecrypt;
import nl.han.asd.project.client.store.IMessage;

public class MessageProcessingService implements IReceiveMessage {
    public IConfirmationMessageBuilder confirmationMessageBuilder;
    public IDecrypt decrypt;
    public IMessage message;
}
