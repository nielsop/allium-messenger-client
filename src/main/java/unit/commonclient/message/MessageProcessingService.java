package unit.commonclient.message;

import unit.commonclient.cryptography.IDecrypt;
import unit.commonclient.store.IMessage;

public class MessageProcessingService implements IReceiveMessage {
    public IConfirmationMessageBuilder confirmationMessageBuilder;
    public IDecrypt decrypt;
    public IMessage message;
}
