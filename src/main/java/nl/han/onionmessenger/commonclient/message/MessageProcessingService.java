package nl.han.onionmessenger.commonclient.message;

import nl.han.onionmessenger.commonclient.cryptography.IDecrypt;
import nl.han.onionmessenger.commonclient.store.IMessage;

public class MessageProcessingService implements IReceiveMessage {
    public IConfirmationMessageBuilder confirmationMessageBuilder;
    public IDecrypt decrypt;
    public IMessage message;
}
